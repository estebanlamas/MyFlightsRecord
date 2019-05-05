package com.estebanlamas.myflightsrecorder.presentation.map

import com.estebanlamas.myflightsrecorder.domain.model.PlanePosition

interface MapView {
    fun showTrack(track: List<PlanePosition>)
}