package com.amit4app.weatherforecast.data.remote.models

data class GeoResponse(val results: List<GeoItem>?)
data class GeoItem(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val country: String?,
    val admin1: String?
)
