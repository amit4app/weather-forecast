package com.amit4app.weatherforecast.data.local

import androidx.room.*

@Dao
interface ForecastDao {
    @Query("SELECT * FROM forecast WHERE city = :city ORDER BY dateIso")
    suspend fun getForCity(city: String): List<ForecastEntity>

    @Query("DELETE FROM forecast WHERE city = :city")
    suspend fun deleteForCity(city: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<ForecastEntity>)
}
