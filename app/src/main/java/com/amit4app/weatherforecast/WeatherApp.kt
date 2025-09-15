package com.amit4app.weatherforecast

import android.app.Application
import androidx.room.Room
import com.amit4app.weatherforecast.data.local.AppDb

class WeatherApp : Application() {
    lateinit var db: AppDb
        private set

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(this, AppDb::class.java, "weather.db")
            .fallbackToDestructiveMigration()
            .build()
    }
}
