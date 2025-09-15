package com.amit4app.weatherforecast.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiFactory {
    private val ok = OkHttpClient.Builder().build()

    private val geoRetrofit = Retrofit.Builder()
        .baseUrl("https://geocoding-api.open-meteo.com/")
        .client(ok)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val weatherRetrofit = Retrofit.Builder()
        .baseUrl("https://api.open-meteo.com/")
        .client(ok)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val geo: GeoApi = geoRetrofit.create(GeoApi::class.java)
    val weather: WeatherApi = weatherRetrofit.create(WeatherApi::class.java)
}
