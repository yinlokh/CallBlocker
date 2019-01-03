package callblocker.callblocker.core

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.util.Log

import callblocker.callblocker.R

class StickyService : Service() {

    companion object {
        const val NOTIFICATION_ID = 12321
    }

    private var receiver: CallBroadcastReceiver? = null

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        val notificationBuilder = NotificationCompat.Builder(this)
        notificationBuilder.setSmallIcon(R.drawable.call_received)
        notificationBuilder.setContentTitle("Call blocking active")
        notificationBuilder.setChannelId(NotificationChannels.PRIORITY_HIGH)
        startForeground(NOTIFICATION_ID, notificationBuilder.build())

        val phoneStateFilter = IntentFilter()
        phoneStateFilter.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED)
        receiver = CallBroadcastReceiver(this)
        registerReceiver(receiver, phoneStateFilter)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        return Service.START_STICKY
    }

    override fun onDestroy() {
        if (receiver != null) {
            unregisterReceiver(receiver)
            receiver = null
        }

        super.onDestroy()
    }
}
