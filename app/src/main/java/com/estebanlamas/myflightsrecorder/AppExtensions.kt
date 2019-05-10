package com.estebanlamas.myflightsrecorder

import android.location.Location
import android.os.Build
import com.estebanlamas.myflightsrecorder.domain.model.PlanePosition
import java.text.SimpleDateFormat
import java.util.*

fun versionIs26OrBigger() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O

fun Date.formatDMMMMYYYY(): String {
    val simpleDateFormat = SimpleDateFormat("d MMMM YYYY")
    return simpleDateFormat.format(this)
}

fun PlanePosition.heading(planePosition: PlanePosition): Float {
    return this.getLocation().bearingTo(planePosition.getLocation())
}

fun PlanePosition.getLocation(): Location {
    val location = Location("")
    location.altitude = this.altitude
    location.latitude = this.latitude
    location.longitude = this.longitude
    return location
}