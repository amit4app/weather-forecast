package com.amit4app.weatherforecast.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "forecast")
data class ForecastEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val city: String,
    val dateIso: String,     // yyyy-MM-dd
    val tempMax: Double,
    val tempMin: Double,
    val weatherCode: Int,
    val fetchedAt: Long
)
