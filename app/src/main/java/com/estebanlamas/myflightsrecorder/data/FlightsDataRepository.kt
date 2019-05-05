package com.estebanlamas.myflightsrecorder.data

import com.estebanlamas.myflightsrecorder.data.db.FlightDAO
import com.estebanlamas.myflightsrecorder.data.db.FlightEntity
import com.estebanlamas.myflightsrecorder.data.db.PlanePositionDAO
import com.estebanlamas.myflightsrecorder.data.mapper.planePositionListToDomain
import com.estebanlamas.myflightsrecorder.data.mapper.toData
import com.estebanlamas.myflightsrecorder.data.mapper.toDomain
import com.estebanlamas.myflightsrecorder.domain.model.Flight
import com.estebanlamas.myflightsrecorder.domain.model.PlanePosition
import com.estebanlamas.myflightsrecorder.domain.repository.FlightRepository
import java.util.*

class FlightsDataRepository(val flightDAO: FlightDAO, val planePositionDAO: PlanePositionDAO): FlightRepository {

    override fun createFlight(): Flight {
        val flight = FlightEntity(Date().time, "")
        flightDAO.insert(flight)
        return flight.toDomain()
    }

    override fun addPlanePosition(flightId: Long, planePosition: PlanePosition) {
        val planePositionEntity = planePosition.toData(flightId)
        planePositionDAO.insert(planePositionEntity)
    }

    override fun updateFlight(flight: Flight) {
        flightDAO.update(flight.toData())
    }

    override fun removeFlight(flight: Flight) {
        flightDAO.delete(flight.toData())
    }

    override fun getFlights(): List<Flight> {
        val flightsEntities = flightDAO.loadFlights()
        val flights = flightsEntities.toDomain()
        flights.forEach {
            it.track = planePositionDAO.getFlightPositions(it.id).planePositionListToDomain()
        }
        return flights
    }

    override fun getPlanePositions(flightId: Long): List<PlanePosition> {
        val planePositionsEntities = planePositionDAO.getFlightPositions(flightId)
        return planePositionsEntities.planePositionListToDomain()
    }
}