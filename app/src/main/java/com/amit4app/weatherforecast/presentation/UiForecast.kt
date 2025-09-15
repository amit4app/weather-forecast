package com.amit4app.weatherforecast.presentation

import androidx.compose.ui.graphics.vector.ImageVector

data class UiForecast(
    val date: String,
    val min: String,
    val max: String,
    val ui: WeatherUi
)

data class WeatherUi(
    val label: String,
    val icon: ImageVector,
    val codeHint: Int
)
