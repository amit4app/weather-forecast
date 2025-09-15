package com.amit4app.weatherforecast.domain.model

data class Forecast(
    val city: String,
    val days: List<DailyForecast>
)

data class DailyForecast(
    val dateIso: String,
    val tempMax: Double,
    val tempMin: Double,
    val weatherCode: Int
)
