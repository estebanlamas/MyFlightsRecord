package com.estebanlamas.myflightsrecorder.presentation

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.estebanlamas.myflightsrecorder.R
import com.estebanlamas.myflightsrecorder.data.FlightsDataRepository
import com.estebanlamas.myflightsrecorder.data.GoogleLocationRepository
import com.estebanlamas.myflightsrecorder.domain.model.PlanePosition
import com.estebanlamas.myflightsrecorder.domain.repository.LocationRepository

class RecorderService: Service(), LocationRepository.LocationCallbacks {

    companion object {
        const val NOTIFICATION_ID = 340
        const val CHANNEL_ID = "recorderservice"
        const val TAG = "RecorderService"

        fun getIntent(context: Context): Intent {
            return Intent(context, RecorderService::class.java)
        }
    }

    private val locationRepository = GoogleLocationRepository(this)
    private val flightRepository = FlightsDataRepository(this)
    private val flight = flightRepository.createFlight()
    // region Service

    override fun onCreate() {
        super.onCreate()
        if (versionIs26OrBigger()) {
            startForeground(NOTIFICATION_ID, createNotification())
        }

        locationRepository.initUpdates(this)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        return Service.START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        if (versionIs26OrBigger()) {
            stopForeground(true)
        }
        locationRepository.stopUpdates()
        super.onDestroy()
    }

    // endregion

    // region Private methods

    private fun createNotification(): Notification {
        createNotificationChannel()
        val builder = NotificationCompat.Builder(this,
            CHANNEL_ID
        )
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

    private fun versionIs26OrBigger() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O

    // endregion

    // region LocationRepository

    override fun updateLocation(location: PlanePosition) {
        flightRepository.addPlanePosition(flightId = flight.id, planePosition = location)
    }

    override fun error() {
        if(!flight.hasTrack()) {
            flightRepository.removeFlight(flight.id)
        }
    }

    // endregion
}