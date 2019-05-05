package com.estebanlamas.myflightsrecorder.presentation

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import com.estebanlamas.myflightsrecorder.domain.model.Flight
import com.estebanlamas.myflightsrecorder.domain.model.PlanePosition
import com.estebanlamas.myflightsrecorder.domain.repository.FlightRepository
import com.estebanlamas.myflightsrecorder.domain.repository.LocationRepository
import com.estebanlamas.myflightsrecorder.versionIs26OrBigger
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import kotlin.coroutines.CoroutineContext

class RecorderService: Service(), LocationRepository.LocationCallbacks, CoroutineScope {

    companion object {
        const val NOTIFICATION_ID = 340
        const val CHANNEL_ID = "recorderservice"
        const val TAG = "RecorderService"

        fun getIntent(context: Context): Intent {
            return Intent(context, RecorderService::class.java)
        }
    }
    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val locationRepository: LocationRepository by inject()
    private val flightRepository: FlightRepository by inject()
    private lateinit var flight: Flight
    private val notificationManager = NotificationManager(this)

    // region Service

    override fun onCreate() {
        super.onCreate()
        if (versionIs26OrBigger()) {
            startForeground(NOTIFICATION_ID, notificationManager.createNotification())
        }
        job = Job()
        launch {
            flight = withContext(Dispatchers.IO) {
                flightRepository.createFlight()
            }
            locationRepository.initUpdates(this@RecorderService)
        }
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
        launch {
            async(Dispatchers.IO) {
                flightRepository.addPlanePosition(flightId = flight.id, planePosition = location)
            }
        }
    }

    override fun error() {
        if(!flight.hasTrack()) {
            flightRepository.removeFlight(flight)
        }
    }

    // endregion
}