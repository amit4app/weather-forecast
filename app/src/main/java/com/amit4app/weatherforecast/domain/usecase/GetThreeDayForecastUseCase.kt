package com.amit4app.weatherforecast.domain.usecase

import com.amit4app.weatherforecast.data.WeatherRepository

class GetThreeDayForecastUseCase(private val repo: WeatherRepository) {
    suspend operator fun invoke(city: String) = repo.getThreeDayForecast(city)
}
