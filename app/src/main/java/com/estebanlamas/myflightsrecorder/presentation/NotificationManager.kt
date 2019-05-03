package com.estebanlamas.myflightsrecorder.presentation

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.estebanlamas.myflightsrecorder.R
import com.estebanlamas.myflightsrecorder.versionIs26OrBigger

class NotificationManager(private val context: Context) {

    fun createNotification(): Notification {
        createNotificationChannel()
        val builder = NotificationCompat.Builder(context,
            RecorderService.CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText("Recording flight...")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        return builder.build()
    }

    private fun createNotificationChannel() {
        if (versionIs26OrBigger()) {
            val name = context.getString(R.string.app_name)
            val descriptionText = "Record flight"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(RecorderService.CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}