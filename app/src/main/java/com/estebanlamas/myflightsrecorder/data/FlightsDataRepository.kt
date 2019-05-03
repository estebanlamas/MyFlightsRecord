package com.estebanlamas.myflightsrecorder.data

import android.content.Context
import android.util.Log
import com.estebanlamas.myflightsrecorder.domain.model.Flight
import com.estebanlamas.myflightsrecorder.domain.model.PlanePosition
import com.estebanlamas.myflightsrecorder.domain.repository.FlightRepository
import com.estebanlamas.myflightsrecorder.presentation.RecorderService

class FlightsDataRepository(context: Context): FlightRepository {
    override fun createFlight(): Flight {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addPlanePosition(flightId: Long, planePosition: PlanePosition) {
        Log.d(RecorderService.TAG, "${planePosition.latitude} ${planePosition.longitude} ${planePosition.altitude}")
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateFlight(flight: Flight) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeFlight(flightId: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}