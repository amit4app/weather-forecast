package com.amit4app.weatherforecast.data.mapper

import com.amit4app.weatherforecast.data.local.ForecastEntity
import com.amit4app.weatherforecast.data.remote.models.WeatherResponse
import com.amit4app.weatherforecast.domain.model.DailyForecast
import com.amit4app.weatherforecast.domain.model.Forecast

fun WeatherResponse.toEntities(city: String): List<ForecastEntity> {
    val d = daily ?: return emptyList()
    val now = System.currentTimeMillis()
    return d.time.indices.map { i ->
        ForecastEntity(
            city = city,
            dateIso = d.time[i],
            tempMax = d.temperature_2m_max[i],
            tempMin = d.temperature_2m_min[i],
            weatherCode = d.weathercode[i],
            fetchedAt = now
        )
    }
}

fun List<ForecastEntity>.toDomain(city: String): Forecast? {
    if (isEmpty()) return null
    return Forecast(
        city = city,
        days = map {
            DailyForecast(
                dateIso = it.dateIso,
                tempMax = it.tempMax,
                tempMin = it.tempMin,
                weatherCode = it.weatherCode
            )
        }
    )
}
