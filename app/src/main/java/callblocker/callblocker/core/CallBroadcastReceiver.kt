package callblocker.callblocker.core

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.support.annotation.RequiresApi
import android.telecom.TelecomManager
import android.telephony.TelephonyManager

import java.lang.reflect.Method

import callblocker.callblocker.database.CallHistoryStore
import callblocker.callblocker.database.FilterListStore
import callblocker.callblocker.models.CallHistory

/**
 * Broadcast receiver for phone state changes, since this is an implicit broadcast receiver, it will
 * need to be registered during application runtime instead of on manifest.
 */
class CallBroadcastReceiver(context: Context, val listener : Listener) : BroadcastReceiver() {

    companion object {

        val ACTION_PAUSE_BLOCKING = "callblocker.pause"
        val ACTION_RESUME_BLOCKING = "callblocker.resume"
    }

    private val callHistoryStore: CallHistoryStore
    private val contactsChecker: ContactsChecker
    private val filterListStore: FilterListStore
    private val preferences: CallBlockerPreferences
    private val phoneNumberChecker: PhoneNumberChecker

    private var running = true

    init {
        callHistoryStore = CallHistoryStore(context)
        filterListStore = FilterListStore(context)
        preferences = CallBlockerPreferences(context)
        phoneNumberChecker = PhoneNumberChecker(filterListStore.filterEntries)
        contactsChecker = ContactsChecker(context)
    }

    override fun onReceive(context: Context, intent: Intent) {
        when(intent.action) {
            TelephonyManager.ACTION_PHONE_STATE_CHANGED -> handleCallBroadcast(context, intent)
            ACTION_PAUSE_BLOCKING -> changeCallBlockingState(true)
            ACTION_RESUME_BLOCKING -> changeCallBlockingState(false)
        }

    }

    private fun handleCallBroadcast(context: Context, intent: Intent) {
        if (!running) {
            return
        }

        try {
            val stateStr = intent.extras.getString(TelephonyManager.EXTRA_STATE)
            val incomingNumber = intent.extras.getString(TelephonyManager.EXTRA_INCOMING_NUMBER)
            if (TelephonyManager.EXTRA_STATE_RINGING != stateStr || incomingNumber == null) {
                return
            }

            val whitelistedContact = preferences.whitelistAllContacts && contactsChecker.phoneIsInContacts(incomingNumber)
            val shouldBlock = phoneNumberChecker.shouldBlockNumber(incomingNumber) && !whitelistedContact

            callHistoryStore.addCallHistory(
                    CallHistory.create(System.currentTimeMillis(), incomingNumber, shouldBlock))
            if (shouldBlock) {
                endCall(context)
            }
        } catch (e: Exception) {
            // do something???
        }
    }

    // Logic copied from stack overflow..
    private fun endCall(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            endCallAPI28(context)
        } else {
            endCallAPI1(context)
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private fun endCallAPI28(context: Context): Boolean {
        val tm = context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager

        return if (tm != null && context.checkSelfPermission(Manifest.permission.ANSWER_PHONE_CALLS) == PackageManager.PERMISSION_GRANTED) {
            tm.endCall()
        } else false
    }

    private fun endCallAPI1(context: Context): Boolean {
        try {
            val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

            val classTelephony = Class.forName(telephonyManager.javaClass.name)
            val methodGetITelephony = classTelephony.getDeclaredMethod("getITelephony")

            methodGetITelephony.isAccessible = true

            val telephonyInterface = methodGetITelephony.invoke(telephonyManager)

            val telephonyInterfaceClass = Class.forName(telephonyInterface.javaClass.name)
            val methodEndCall = telephonyInterfaceClass.getDeclaredMethod("endCall")

            methodEndCall.invoke(telephonyInterface)

        } catch (ex: Exception) { // Many things can go wrong with reflection calls
            return false
        }

        return true
    }

    private fun changeCallBlockingState(paused: Boolean) {
        running = !paused
        listener.callBlockingStateChanged(paused)
    }

    interface Listener {

        fun callBlockingStateChanged(paused : Boolean)
    }
}
