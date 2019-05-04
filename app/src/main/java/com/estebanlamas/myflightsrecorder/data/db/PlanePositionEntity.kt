package com.estebanlamas.myflightsrecorder.data.db

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(tableName = "positions",foreignKeys = [ForeignKey(
                        entity = FlightEntity::class,
                        parentColumns = ["id"],
                        childColumns = ["flightId"],
                        onDelete = CASCADE)])
data class PlanePositionEntity(
    @PrimaryKey
    @NonNull
    var date: Long,
    var latitude: Double,
    var longitude: Double,
    var altitude: Double,
    var heading: Float,
    var flightId: Long
)