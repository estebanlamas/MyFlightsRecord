package com.estebanlamas.myflightsrecorder.domain.model

import java.io.Serializable

class Flight(
    var id: Long,
    var name: String = "",
    var track: List<PlanePosition> = listOf()
): Serializable {
    fun hasTrack() = track.isNotEmpty()
}