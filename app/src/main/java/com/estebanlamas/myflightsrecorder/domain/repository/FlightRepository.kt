package com.estebanlamas.myflightsrecorder.domain.repository

import com.estebanlamas.myflightsrecorder.domain.model.Flight
import com.estebanlamas.myflightsrecorder.domain.model.PlanePosition

interface FlightRepository {
    fun createFlight(): Flight
    fun addPlanePosition(flightId: Long, planePosition: PlanePosition)
    fun updateFlight(flight: Flight)
    fun removeFlight(flightId: Long)
}