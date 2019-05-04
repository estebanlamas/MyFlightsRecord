package com.estebanlamas.myflightsrecorder.domain.repository

import com.estebanlamas.myflightsrecorder.domain.model.Flight
import com.estebanlamas.myflightsrecorder.domain.model.PlanePosition

interface FlightRepository {
    fun createFlight(): Flight
    fun updateFlight(flight: Flight)
    fun removeFlight(flight: Flight)
    fun getFlights(): List<Flight>
    fun getPlanePositions(flightId: Long): List<PlanePosition>
    fun addPlanePosition(flightId: Long, planePosition: PlanePosition)
}