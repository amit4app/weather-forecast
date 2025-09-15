package com.amit4app.weatherforecast.data

import com.amit4app.weatherforecast.data.local.*
import com.amit4app.weatherforecast.data.mapper.toDomain
import com.amit4app.weatherforecast.data.remote.GeoApi
import com.amit4app.weatherforecast.data.remote.WeatherApi
import com.amit4app.weatherforecast.domain.model.DailyForecast
import com.amit4app.weatherforecast.domain.model.Forecast
import com.amit4app.weatherforecast.domain.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DefaultWeatherRepository(
    private val forecastDao: ForecastDao,
    private val cityDao: CityDao?,
    private val geoApi: GeoApi,
    private val weatherApi: WeatherApi
) : WeatherRepository {

    private fun normalize(city: String) =
        city.substringBefore(',')
            .trim()
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }

    override suspend fun getThreeDayForecast(city: String): Result<Forecast> = withContext(Dispatchers.IO) {
        val norm = normalize(city)
        try {
            val g = geoApi.search(norm).results?.firstOrNull()
                ?: return@withContext Result.Error<Forecast>(IllegalArgumentException("City not found"))

            cityDao?.upsertAll(
                listOf(
                    CityEntity(
                        name = g.name ?: norm,
                        country = g.country,
                        admin1 = g.admin1,
                        latitude = g.latitude,
                        longitude = g.longitude
                    )
                )
            )

            val resp = weatherApi.forecast(g.latitude, g.longitude)
            val daily = resp.daily ?: return@withContext Result.Error<Forecast>(IllegalStateException("No forecast"))

            val now = System.currentTimeMillis()
            val entities = daily.time.indices.map { i ->
                ForecastEntity(
                    city = norm,
                    dateIso = daily.time[i],
                    tempMax = daily.temperature_2m_max[i],
                    tempMin = daily.temperature_2m_min[i],
                    weatherCode = daily.weathercode[i],
                    fetchedAt = now
                )
            }

            forecastDao.deleteForCity(norm)
            forecastDao.upsertAll(entities)

            val domain = Forecast(
                city = norm,
                days = entities.map {
                    DailyForecast(
                        dateIso = it.dateIso,
                        tempMin = it.tempMin,
                        tempMax = it.tempMax,
                        weatherCode = it.weatherCode
                    )
                }
            )
            Result.Success(domain)
        } catch (t: Throwable) {
            val cached = forecastDao.getForCity(norm)
            if (cached.isNotEmpty()) {
                Result.Error(t, cached.toDomain(norm))
            } else {
                Result.Error(t, null)
            }
        }
    }
}
