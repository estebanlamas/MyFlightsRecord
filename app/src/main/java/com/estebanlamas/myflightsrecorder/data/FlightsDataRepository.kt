package com.estebanlamas.myflightsrecorder.data

import android.content.Context
import android.util.Log
import com.estebanlamas.myflightsrecorder.data.db.AppDatabase
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

class FlightsDataRepository(val context: Context): FlightRepository {

    lateinit var flightDAO: FlightDAO
    lateinit var planePositionDAO: PlanePositionDAO

    init {
        val appDatabase = AppDatabase.getDatabase(context = context)
        appDatabase?.let {
            flightDAO = it.flightDAO()
            planePositionDAO = it.planePositionDAO()
        }
    }

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
        return flightsEntities.toDomain()
    }

    override fun getPlanePositions(flightId: Long): List<PlanePosition> {
        val planePositionsEntities = planePositionDAO.getFlightPositions(flightId)
        return planePositionsEntities.planePositionListToDomain()
    }
}