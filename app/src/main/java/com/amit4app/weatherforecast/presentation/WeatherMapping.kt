package com.amit4app.weatherforecast.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Grain
import androidx.compose.material.icons.filled.WbSunny
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun mapWeather(code: Int): WeatherUi = when (code) {
    0 -> WeatherUi("Clear", Icons.Filled.WbSunny, code)
    1, 2 -> WeatherUi("Mostly clear", Icons.Filled.WbSunny, code)
    3 -> WeatherUi("Cloudy", Icons.Filled.Cloud, code)
    45, 48 -> WeatherUi("Fog", Icons.Filled.Cloud, code)
    51,53,55,56,57,61,63,65,66,67,80,81,82 -> WeatherUi("Rain", Icons.Filled.Grain, code)
    71,73,75,77,85,86 -> WeatherUi("Snow", Icons.Filled.AcUnit, code)
    else -> WeatherUi("Clouds", Icons.Filled.Cloud, code)
}

private val inFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd")
private val outFmt = DateTimeFormatter.ofPattern("EEE, dd MMM")

fun prettyDate(iso: String): String =
    runCatching { LocalDate.parse(iso, inFmt).format(outFmt) }.getOrElse { iso }
