package com.estebanlamas.myflightsrecorder.presentation.map

import com.estebanlamas.myflightsrecorder.domain.model.Flight
import com.estebanlamas.myflightsrecorder.domain.model.PlanePosition
import com.estebanlamas.myflightsrecorder.domain.repository.FlightRepository
import com.estebanlamas.myflightsrecorder.heading
import com.estebanlamas.myflightsrecorder.presentation.common.Presenter
import com.estebanlamas.myflightsrecorder.presentation.utils.EditableDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.koin.core.qualifier.named

class MapPresenter(private val flightRepository: FlightRepository): Presenter<MapView>(), EditableDialog.Callback {
    var track: List<PlanePosition> = listOf()
    lateinit var flight: Flight

    fun requestPlanePositions(flight: Flight) {
        this.flight = flight
        setTitle()
        requestTrack()
    }

    private fun setTitle() {
        if(flight.name.isNotEmpty()){
            view?.setToolbar(flight.name)
        }
    }

    private fun requestTrack() {
        launch {
            val trackResult = async(Dispatchers.IO) {
                flightRepository.getPlanePositions(flight.id)
            }
            track = trackResult.await()
            if(track.isNotEmpty()) {
                view?.showTrack(track)
                view?.showAltitudeChart(track)
            }
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

    fun onClickEdit() {
        view?.showChangeNameDialog(flight.name)
    }

    // region EditableDialog.Callback

    override fun onAcceptChange(newName: String) {
        flight.name = newName
        launch {
            async(Dispatchers.IO) {
                flightRepository.updateFlight(flight)
            }
            view?.setToolbar(newName)
        }
    }

    // endregion
}