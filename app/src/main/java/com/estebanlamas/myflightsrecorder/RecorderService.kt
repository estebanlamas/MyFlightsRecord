package com.estebanlamas.myflightsrecorder

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log

import androidx.core.app.NotificationCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

// https://github.com/BlackBlind567/Location_Updates-Background_Foreground/blob/master/app/src/main/java/com/atomsindia/local/LocationUpdatesService.java
class RecorderService: Service() {
    companion object {
        const val NOTIFICATION_ID = 340
        const val CHANNEL_ID = "recorderservice"
        const val TAG = "RecorderService"

        fun getIntent(context: Context): Intent {
            return Intent(context, RecorderService::class.java)
        }
    }

    val fusedLocationProviderClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }

    var continueService = true

    override fun onCreate() {
        super.onCreate()
        if (versionIs26OrBigger()) {
            startForeground(NOTIFICATION_ID, createNotification())
        }
        Log.d(TAG, "onCreate service")
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Thread(Runnable {
            try {
                while(continueService) {
                    Thread.sleep(1000)
                    Log.d("Tag", "running...")
                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }).start()
        return Service.START_STICKY
    }

    private fun createNotification(): Notification {
        createNotificationChannel()
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(getString(R.string.app_name))
            .setContentText("Recording flight...")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        return builder.build()
    }

    private fun createNotificationChannel() {
        if (versionIs26OrBigger()) {
            val name = getString(R.string.app_name)
            val descriptionText = "Record flight"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    @Override
    override fun onDestroy() {
        if (versionIs26OrBigger()) {
            stopForeground(true)
        }
        Log.d(TAG, "onDestroy service")
        continueService = false
        super.onDestroy()
    }

    private fun versionIs26OrBigger() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
}