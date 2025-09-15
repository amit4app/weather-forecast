package com.amit4app.weatherforecast.data.remote.models

data class WeatherResponse(
    val daily: DailyBlock?
)

data class DailyBlock(
    val time: List<String>,
    val temperature_2m_max: List<Double>,
    val temperature_2m_min: List<Double>,
    val weathercode: List<Int>
)
