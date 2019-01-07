package callblocker.callblocker.core

data class NotificationData(
        val contentTitleResId: Int,
        val iconResId: Int,
        val actionTitleResId: Int,
        val actionIconResId: Int,
        val action: String)