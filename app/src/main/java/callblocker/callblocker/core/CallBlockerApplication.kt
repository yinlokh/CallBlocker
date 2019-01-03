package callblocker.callblocker.core

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.content.Context
import android.os.Build

class CallBlockerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        setupNotificationChannels()
    }

    private fun setupNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannelManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notificationChannel = NotificationChannel(
                    NotificationChannels.PRIORITY_HIGH,"high priority", IMPORTANCE_HIGH)
            notificationChannelManager.createNotificationChannel(notificationChannel)
        }
    }
}
