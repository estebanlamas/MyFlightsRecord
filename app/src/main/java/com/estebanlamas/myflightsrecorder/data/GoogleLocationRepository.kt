package com.estebanlamas.myflightsrecorder.data

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.estebanlamas.myflightsrecorder.domain.model.PlanePosition
import com.estebanlamas.myflightsrecorder.domain.repository.LocationRepository
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import java.util.*

class GoogleLocationRepository(private val context: Context): LocationRepository,
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener {

    companion object {
        const val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 4 * 1000
        const val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS: Long = 2 * 1000
    }

    lateinit var callbacks: LocationRepository.LocationCallbacks

    private val fusedLocationProviderClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    private val googleApiClient: GoogleApiClient by lazy {
        GoogleApiClient
            .Builder(context)
            .addApi(LocationServices.API)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .build()
    }

    private val locationCallback = object: LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            val location = result.locations[0]
            val myLocation = PlanePosition(
                date = Date(),
                latitude = location.latitude,
                longitude = location.longitude,
                altitude = location.altitude,
                heading = location.bearing
            )
            callbacks.updateLocation(myLocation)
        }
    }

    override fun initUpdates(callbacks: LocationRepository.LocationCallbacks) {
        googleApiClient.connect()
        this.callbacks = callbacks
    }

    override fun stopUpdates() {
        if(googleApiClient.isConnected) {
            googleApiClient.disconnect()
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }

    // region ConnectionCallbacks

    override fun onConnected(bundle: Bundle?) {
        val locationRequest = LocationRequest()
        locationRequest.interval =
            UPDATE_INTERVAL_IN_MILLISECONDS
        locationRequest.fastestInterval =
            FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        if ((ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }

    override fun onConnectionSuspended(p0: Int) {
        callbacks.error()
    }

    // endregion

    override fun onConnectionFailed(p0: ConnectionResult) {
        callbacks.error()
    }
}