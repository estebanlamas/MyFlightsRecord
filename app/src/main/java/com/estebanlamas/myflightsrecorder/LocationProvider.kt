package com.estebanlamas.myflightsrecorder

interface LocationProvider {
    fun initUpdates(callbacks: LocationCallbacks)
    fun stopUpdates()

    interface LocationCallbacks {
        fun updateLocation(location: MyLocation)
        fun error()
    }
}