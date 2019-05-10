package com.estebanlamas.myflightsrecorder.presentation.map

import com.estebanlamas.myflightsrecorder.domain.model.Flight
import com.estebanlamas.myflightsrecorder.domain.model.PlanePosition
import com.estebanlamas.myflightsrecorder.domain.repository.FlightRepository
import com.estebanlamas.myflightsrecorder.heading
import com.estebanlamas.myflightsrecorder.presentation.common.Presenter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MapPresenter(private val flightRepository: FlightRepository): Presenter<MapView>() {
    var track: List<PlanePosition> = listOf()

    fun requestPlanePositions(flight: Flight) {
        launch {
            val trackResult = async(Dispatchers.IO) {
                flightRepository.getPlanePositions(flight.id)
            }
            track = trackResult.await()
            view?.showTrack(track)
            view?.showAltitudeChart(track)
        }
    }

    fun onSelectTime(time: Long) {
        val date = time + track[0].date.time
        val planePosition = track.find { date == it.date.time }
        val nextPlanePlanePosition = track[track.indexOf(planePosition)+1]
        if(planePosition!=null) {
            val bearing = planePosition.heading(nextPlanePlanePosition)
            view?.showPlanePostion(planePosition, bearing)
        }
    }
}