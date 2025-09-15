package com.amit4app.weatherforecast.data.remote

import com.amit4app.weatherforecast.data.remote.models.GeoResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GeoApi {
    // Open-Meteo geocoding
    @GET("v1/search")
    suspend fun search(
        @Query("name") name: String,
        @Query("count") count: Int = 5
    ): GeoResponse
}
