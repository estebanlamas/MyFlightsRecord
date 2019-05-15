package com.estebanlamas.myflightsrecorder.presentation.utils

import android.app.Activity
import android.content.Context
import android.location.LocationManager
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationRequest


class GpsUtils {

    companion object {
        private const val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 4 * 1000
        private const val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS: Long = 2 * 1000
        const val LOCATION_REQUEST = 777

        fun turnGPSOn(activity: Activity, onGpsListener: OnGpsListener) {
            val locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                onGpsListener.gpsStatus(true)
            } else {
                LocationServices.getSettingsClient(activity)
                    .checkLocationSettings(getLocationSettingsRequest(getLocationRequest()))
                    .addOnSuccessListener(activity) {
                        onGpsListener.gpsStatus(true)
                    }
                    .addOnFailureListener(activity) { e ->
                        when ((e as ApiException).statusCode) {
                            LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                                val res = e as ResolvableApiException
                                res.startResolutionForResult(activity, LOCATION_REQUEST)
                            }
                            LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {

                            }
                        }
                    }
            }
        }

        private fun getLocationSettingsRequest(locationRequest: LocationRequest): LocationSettingsRequest {
            val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
            val locationSettingsRequest = builder.build()
            builder.setAlwaysShow(true)
            return locationSettingsRequest
        }

        fun getLocationRequest(): LocationRequest {
            val locationRequest = LocationRequest.create()
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            locationRequest.interval = UPDATE_INTERVAL_IN_MILLISECONDS
            locationRequest.fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
            return locationRequest
        }
    }

    interface OnGpsListener {
        fun gpsStatus(isGPSEnable: Boolean)
    }
}
