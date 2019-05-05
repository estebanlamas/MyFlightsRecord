package com.estebanlamas.myflightsrecorder.presentation.flights

import com.estebanlamas.myflightsrecorder.domain.model.Flight

interface FlightsListView {
    fun showFlights(flights: List<Flight>)
}