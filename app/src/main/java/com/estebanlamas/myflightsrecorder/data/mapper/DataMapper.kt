package com.estebanlamas.myflightsrecorder.data.mapper

import com.estebanlamas.myflightsrecorder.data.db.FlightEntity
import com.estebanlamas.myflightsrecorder.data.db.PlanePositionEntity
import com.estebanlamas.myflightsrecorder.domain.model.Flight
import com.estebanlamas.myflightsrecorder.domain.model.PlanePosition
import java.util.*

fun FlightEntity.toDomain(): Flight {
    return Flight(
        id = id,
        name = name)
}

fun Flight.toData(): FlightEntity {
    return FlightEntity(
        id = id,
        name = name)
}

fun List<FlightEntity>.toDomain(): List<Flight> {
    val list = arrayListOf<Flight>()
    forEach { list.add(it.toDomain()) }
    return list
}

fun PlanePositionEntity.toDomain(): PlanePosition {
    return PlanePosition(
        date = Date(date),
        latitude = latitude,
        longitude = longitude,
        altitude = altitude,
        heading = heading,
        speed = speed)
}

fun PlanePosition.toData(flightId: Long): PlanePositionEntity {
    return PlanePositionEntity(
        date = date.time,
        latitude = latitude,
        longitude = longitude,
        altitude = altitude,
        heading = heading,
        flightId = flightId,
        speed = speed)
}

fun List<PlanePositionEntity>.planePositionListToDomain(): List<PlanePosition> {
    val list = arrayListOf<PlanePosition>()
    forEach { list.add(it.toDomain()) }
    return list
}