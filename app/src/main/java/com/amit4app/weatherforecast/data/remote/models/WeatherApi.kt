package com.amit4app.weatherforecast.data.remote

import com.amit4app.weatherforecast.data.remote.models.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    // Open-Meteo forecast
    @GET("v1/forecast")
    suspend fun forecast(
        @Query("latitude") lat: Double,
        @Query("longitude") lon: Double,
        @Query("daily") daily: String = "temperature_2m_max,temperature_2m_min,weathercode",
        @Query("forecast_days") days: Int = 3,
        @Query("timezone") tz: String = "auto"
    ): WeatherResponse
}
