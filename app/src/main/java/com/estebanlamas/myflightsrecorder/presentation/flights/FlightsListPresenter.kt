package com.estebanlamas.myflightsrecorder.presentation.flights

import com.estebanlamas.myflightsrecorder.domain.repository.FlightRepository
import com.estebanlamas.myflightsrecorder.presentation.common.Presenter
import kotlinx.coroutines.*

class FlightsListPresenter(var flightRepository: FlightRepository): Presenter<FlightsListView>() {

    fun getFlights() {
        launch {
            val flights = async(Dispatchers.IO) {
                flightRepository.getFlights()
            }
            view?.showFlights(flights.await())
        }
    }
}