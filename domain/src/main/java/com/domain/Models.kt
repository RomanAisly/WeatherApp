package com.domain

data class Weather(
    val temperature: Double,
    val windSpeed: Double
)

data class CityItem(
    val id: Int,
    val name: String,
    val country: String,
    val flagEmoji: String,
    val latitude: Double,
    val longitude: Double
)