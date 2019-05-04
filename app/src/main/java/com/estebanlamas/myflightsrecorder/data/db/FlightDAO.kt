package com.estebanlamas.myflightsrecorder.data.db

import androidx.room.*


@Dao
interface FlightDAO {
    @Query("SELECT * from flights")
    fun loadFlights(): List<FlightEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(flightEntity: FlightEntity)

    @Update
    fun update(flightEntity: FlightEntity)

    @Delete
    fun delete(flightEntity: FlightEntity)
}