package callblocker.callblocker.core;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.lang.reflect.Method;

import callblocker.callblocker.database.CallHistoryStore;
import callblocker.callblocker.database.FilterListStore;
import callblocker.callblocker.models.CallHistory;

/**
 * Broadcast receiver for phone state changes, since this is an implicit broadcast receiver, it will
 * need to be registered during application runtime instead of on manifest.
 */
public class CallBroadcastReceiver extends BroadcastReceiver {

    private final CallHistoryStore callHistoryStore;
    private final ContactsChecker contactsChecker;
    private final FilterListStore filterListStore;
    private final CallBlockerPreferences preferences;
    private final PhoneNumberChecker phoneNumberChecker;


    public CallBroadcastReceiver(Context context) {
        callHistoryStore = new CallHistoryStore(context);
        filterListStore = new FilterListStore(context);
        preferences = new CallBlockerPreferences(context);
        phoneNumberChecker = new PhoneNumberChecker(filterListStore.getFilterEntries());
        contactsChecker = new ContactsChecker(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
            String incomingNumber = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
            if (!TelephonyManager.EXTRA_STATE_RINGING.equals(stateStr) || incomingNumber == null) {
                return;
            }

            boolean whitelistedContact = preferences.getWhitelistAllContacts()
                    && contactsChecker.phoneIsInContacts(incomingNumber);
            boolean shouldBlock = phoneNumberChecker.shouldBlockNumber(incomingNumber)
                    && !whitelistedContact;

            callHistoryStore.addCallHistory(
                    CallHistory.create(System.currentTimeMillis(), incomingNumber, shouldBlock));
            if (shouldBlock) {
                Log.e("yinlokh", "killing call");
                killCall(context);
            }
        } catch (Exception e) {
            // do something???
            Log.e("yinlokh", e.toString());
        }
    }

    // Logic copied from stack overflow..
    private boolean killCall(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            return endCallAPI28(context);
        } else {
            return endCallAPI1(context);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private boolean endCallAPI28(Context context) {
        TelecomManager tm = (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE);

        if (tm != null && context.checkSelfPermission(Manifest.permission.ANSWER_PHONE_CALLS) ==
                PackageManager.PERMISSION_GRANTED) {
            return tm.endCall();
        }
        return false;
    }

    private boolean endCallAPI1(Context context) {
        try {
            TelephonyManager telephonyManager =
                    (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            Class classTelephony = Class.forName(telephonyManager.getClass().getName());
            Method methodGetITelephony = classTelephony.getDeclaredMethod("getITelephony");

            methodGetITelephony.setAccessible(true);

            Object telephonyInterface = methodGetITelephony.invoke(telephonyManager);

            Class telephonyInterfaceClass =
                    Class.forName(telephonyInterface.getClass().getName());
            Method methodEndCall = telephonyInterfaceClass.getDeclaredMethod("endCall");

            methodEndCall.invoke(telephonyInterface);

        } catch (Exception ex) { // Many things can go wrong with reflection calls
            return false;
        }
        return true;
    }
}
