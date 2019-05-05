package com.estebanlamas.myflightsrecorder.presentation.flights

import com.estebanlamas.myflightsrecorder.domain.repository.FlightRepository
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class FlightsListPresenter(var flightRepository: FlightRepository): CoroutineScope {

    private lateinit var view: FlightsListView
    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    fun attacheView(view: FlightsListView) {
        this.view = view
        job = Job()
    }

    fun getFlights() {
        launch {
            val flights = async(Dispatchers.IO) {
                flightRepository.getFlights()
            }
            view.showFlights(flights.await())
        }
    }
}