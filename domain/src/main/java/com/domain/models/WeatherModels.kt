package com.domain.models

data class Weather(
    val timezone: String,
    val temperature: Double,
    val windSpeed: Double,
    val weatherCode: Int,
    val isDay: Boolean,
    val precipitation: Double,
    val cloudCover: Int,
    val hourlyWindSpeeds: List<Double>,
    val hourlyWeatherCodes: List<Int>,
    val currentIndex: Int,
    val uvIndex: Double
)

data class WeatherDetails(
    val weather: Weather,
    val windDuration: String,
    val precipDuration: String,
    val weatherDuration: String
)

data class CityItem(
    val id: Int,
    val name: String,
    val country: String,
    val flagEmoji: String,
    val latitude: Double,
    val longitude: Double
)