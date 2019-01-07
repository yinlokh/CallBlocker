package callblocker.callblocker.core

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.telephony.TelephonyManager
import callblocker.callblocker.R

class StickyService : Service(), CallBroadcastReceiver.Listener {

    companion object {
        const val NOTIFICATION_ID = 12321
    }

    private val pausedNotification = NotificationData(
            R.string.notification_call_blocking_paused,
            R.drawable.call_received,
            R.string.notification_action_resume,
            android.R.drawable.ic_media_play,
            CallBroadcastReceiver.ACTION_RESUME_BLOCKING
    )
    private val resumedNotification = NotificationData(
            R.string.notification_call_blocking,
            R.drawable.call_missed,
            R.string.notification_action_pause,
            android.R.drawable.ic_media_pause,
            CallBroadcastReceiver.ACTION_PAUSE_BLOCKING
    )

    private var receiver: CallBroadcastReceiver? = null

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        startForeground(NOTIFICATION_ID, buildNotification(false))

        val phoneStateFilter = IntentFilter()
        phoneStateFilter.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED)
        phoneStateFilter.addAction(CallBroadcastReceiver.ACTION_PAUSE_BLOCKING)
        phoneStateFilter.addAction(CallBroadcastReceiver.ACTION_RESUME_BLOCKING)
        receiver = CallBroadcastReceiver(this, this)
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

    override fun callBlockingStateChanged(paused: Boolean) {
        startForeground(NOTIFICATION_ID, buildNotification(paused))
    }

    private fun buildNotification(paused: Boolean) : Notification {
        val notificationBuilder = NotificationCompat.Builder(this)
        val activityIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, activityIntent, 0)
        notificationBuilder.setContentIntent(pendingIntent)

        val notificationData = if (paused) pausedNotification else resumedNotification
        notificationBuilder.setSmallIcon(notificationData.iconResId)
        notificationBuilder.setContentTitle(getString(notificationData.contentTitleResId))
        notificationBuilder.setChannelId(PRIORITY_HIGH)

        val pauseResumeBroadcast = Intent()
        pauseResumeBroadcast.action = notificationData.action
        val pauseResumeIntent = PendingIntent.getBroadcast(this, 0, pauseResumeBroadcast, 0)
        notificationBuilder.addAction(
                notificationData.actionIconResId,
                getString(notificationData.actionTitleResId),
                pauseResumeIntent
        )
        return notificationBuilder.build()
    }
}
