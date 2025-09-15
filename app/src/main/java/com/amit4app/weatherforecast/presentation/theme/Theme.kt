package com.amit4app.weatherforecast.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Light & dark schemes that use the colors from Colors.kt
private val LightColors = lightColorScheme(
    primary = SkyBlue,
    onPrimary = Color.White,
    secondary = Mint,
    onSecondary = Color.Black,
    background = Cloud,
    surface = Color.White,
    onSurface = Color(0xFF0D1321)
)

private val DarkColors = darkColorScheme(
    primary = Mint,
    onPrimary = Color.Black,
    secondary = Sun,
    onSecondary = Color.Black,
    background = DeepNavy,
    surface = Color(0xFF111736),
    onSurface = Color(0xFFE6ECF4)
)

@Composable
fun WeatherTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),  // default so you can call WeatherTheme { ... }
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}
