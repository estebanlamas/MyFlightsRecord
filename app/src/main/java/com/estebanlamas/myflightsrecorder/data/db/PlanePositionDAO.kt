package com.estebanlamas.myflightsrecorder.data.db

import androidx.room.*


@Dao
interface PlanePositionDAO {

    @Query("SELECT * from positions WHERE flightId=:flightEntityId")
    fun getFlightPositions(flightEntityId: Long): List<PlanePositionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(planePositionEntity: PlanePositionEntity)
}