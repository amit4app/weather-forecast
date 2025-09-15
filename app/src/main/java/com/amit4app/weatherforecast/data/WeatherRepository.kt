package com.amit4app.weatherforecast.data

import com.amit4app.weatherforecast.domain.model.Forecast
import com.amit4app.weatherforecast.domain.util.Result

interface WeatherRepository {
    suspend fun getThreeDayForecast(city: String): Result<Forecast>
}