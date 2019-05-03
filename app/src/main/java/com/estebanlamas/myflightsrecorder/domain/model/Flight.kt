package com.estebanlamas.myflightsrecorder.domain.model

import java.util.*

class Flight(
    var id: Long,
    var name: String = "",
    var date: Date,
    var track: List<PlanePosition> = listOf()
) {
    fun hasTrack() = track.isNotEmpty()
}