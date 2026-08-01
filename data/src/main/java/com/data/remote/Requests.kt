package com.data.remote

data class WeatherRequest(
    val latitude: Double,
    val longitude: Double,
    // По умолчанию запрашиваем эти два параметра, но можем переопределить
    val current: String = "temperature_2m,wind_speed_10m"
)