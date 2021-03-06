package com.estebanlamas.myflightsrecorder.domain.model

import java.io.Serializable
import java.util.*

data class PlanePosition(
    var date: Date,
    var latitude: Double,
    var longitude: Double,
    var altitude: Double,
    var heading: Float,
    var speed: Float
): Serializable