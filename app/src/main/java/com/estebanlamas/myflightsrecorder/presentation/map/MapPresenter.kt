package com.estebanlamas.myflightsrecorder.presentation.map

import com.estebanlamas.myflightsrecorder.domain.model.Flight
import com.estebanlamas.myflightsrecorder.domain.repository.FlightRepository
import com.estebanlamas.myflightsrecorder.presentation.common.Presenter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MapPresenter(val flightRepository: FlightRepository): Presenter<MapView>() {
    fun requestPlanePositions(flight: Flight) {
        launch {
            val track = async(Dispatchers.IO) {
                flightRepository.getPlanePositions(flight.id)
            }
            view?.showTrack(track.await())
        }
    }
}