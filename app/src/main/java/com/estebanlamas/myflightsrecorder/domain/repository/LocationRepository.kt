package com.estebanlamas.myflightsrecorder.domain.repository

import com.estebanlamas.myflightsrecorder.domain.model.PlanePosition

interface LocationRepository {
    fun initUpdates(callbacks: LocationCallbacks)
    fun stopUpdates()

    interface LocationCallbacks {
        fun updateLocation(location: PlanePosition)
        fun error()
    }
}