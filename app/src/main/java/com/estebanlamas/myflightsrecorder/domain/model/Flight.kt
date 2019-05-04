package com.estebanlamas.myflightsrecorder.domain.model

class Flight(
    var id: Long,
    var name: String = "",
    var track: List<PlanePosition> = listOf()
) {
    fun hasTrack() = track.isNotEmpty()
}