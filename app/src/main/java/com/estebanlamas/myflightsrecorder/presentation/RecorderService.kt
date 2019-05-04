package com.estebanlamas.myflightsrecorder.presentation

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import com.estebanlamas.myflightsrecorder.domain.model.PlanePosition
import com.estebanlamas.myflightsrecorder.domain.repository.FlightRepository
import com.estebanlamas.myflightsrecorder.domain.repository.LocationRepository
import com.estebanlamas.myflightsrecorder.versionIs26OrBigger
import org.koin.android.ext.android.inject

class RecorderService: Service(), LocationRepository.LocationCallbacks {

    companion object {
        const val NOTIFICATION_ID = 340
        const val CHANNEL_ID = "recorderservice"
        const val TAG = "RecorderService"

        fun getIntent(context: Context): Intent {
            return Intent(context, RecorderService::class.java)
        }
    }

    private val locationRepository: LocationRepository by inject()
    private val flightRepository: FlightRepository by inject()
    private val flight = flightRepository.createFlight()
    private val notificationManager = NotificationManager(this)

    // region Service

    override fun onCreate() {
        super.onCreate()
        if (versionIs26OrBigger()) {
            startForeground(NOTIFICATION_ID, notificationManager.createNotification())
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

    // region LocationRepository

    override fun updateLocation(location: PlanePosition) {
        flightRepository.addPlanePosition(flightId = flight.id, planePosition = location)
    }

    override fun error() {
        if(!flight.hasTrack()) {
            flightRepository.removeFlight(flight)
        }
    }

    // endregion
}