package com.domain

data class Weather(
    val temperature: Double,
    val windSpeed: Double
)

data class CityItem(
    val name: String,
    val country: String,
    val latitude: Double,
    val longitude: Double
)