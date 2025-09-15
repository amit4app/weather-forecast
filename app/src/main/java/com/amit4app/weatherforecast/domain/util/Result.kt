package com.amit4app.weatherforecast.domain.util

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error<T>(val throwable: Throwable, val cached: T? = null) : Result<T>()
}
