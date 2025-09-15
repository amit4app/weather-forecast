package com.amit4app.weatherforecast.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ForecastEntity::class, CityEntity::class],
    version = 3,
    exportSchema = false
)
abstract class AppDb : RoomDatabase() {
    abstract fun forecastDao(): ForecastDao
    abstract fun cityDao(): CityDao
}
