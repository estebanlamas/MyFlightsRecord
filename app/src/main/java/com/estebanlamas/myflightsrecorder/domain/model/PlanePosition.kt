package com.estebanlamas.myflightsrecorder.domain.model

data class PlanePosition(
    var latitude: Double,
    var longitude: Double,
    var altitude: Double,
    var speed: Float,
    var bearing: Float
)