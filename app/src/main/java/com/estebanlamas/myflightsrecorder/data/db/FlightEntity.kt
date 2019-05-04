package com.estebanlamas.myflightsrecorder.data.db

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flights")
class FlightEntity(
    @PrimaryKey
    @NonNull
    var id: Long,
    var name: String
)