package com.amit4app.weatherforecast.presentation.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Simple palette
private val Sun1 = Color(0xFFFFC371)
private val Sun2 = Color(0xFFFF5F6D)

private val Cloud1 = Color(0xFF89F7FE)
private val Cloud2 = Color(0xFF66A6FF)

private val Rain1 = Color(0xFF5D9FFF)
private val Rain2 = Color(0xFF3A7BD5)

private val Snow1 = Color(0xFFE6F0FF)
private val Snow2 = Color(0xFFBBD2FF)

private val Fog1 = Color(0xFFB6CEE8)
private val Fog2 = Color(0xFFF5F7FA)

fun gradientFor(dominantCode: Int, darkTheme: Boolean): Brush {
    val (c1, c2) = when (dominantCode) {
        0, 1, 2 -> Sun1 to Sun2
        3 -> Cloud1 to Cloud2
        45, 48 -> Fog1 to Fog2
        51,53,55,56,57,61,63,65,66,67,80,81,82 -> Rain1 to Rain2
        71,73,75,77,85,86 -> Snow1 to Snow2
        else -> Cloud1 to Cloud2
    }
    return Brush.verticalGradient(listOf(c1, c2))
}
