package com.estebanlamas.myflightsrecorder.presentation.map

import com.estebanlamas.myflightsrecorder.domain.model.PlanePosition

interface MapView {
    fun showTrack(track: List<PlanePosition>)
    fun showAltitudeChart(track: List<PlanePosition>)
    fun showPlanePostion(planePosition: PlanePosition, heading: Float)
    fun showChangeNameDialog(name: String)
    fun setToolbar(title: String)
    fun flightRemoved()
}