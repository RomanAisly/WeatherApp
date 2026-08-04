package com.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponse(
    @SerialName("current")
    val current: CurrentWeatherDto
)

@Serializable
data class CurrentWeatherDto(
    @SerialName("temperature_2m")
    val temperature: Double,

    @SerialName("wind_speed_10m")
    val windSpeed: Double,

    @SerialName("weather_code")
    val weatherCode: Int,

    @SerialName("is_day")
    val isDay: Int
)

@Serializable
data class GeocodingResponse(
    @SerialName("results") val results: List<GeocodingResult>? = null
)

@Serializable
data class GeocodingResult(
    @SerialName("id") val id: Int,
    @SerialName("latitude") val latitude: Double,
    @SerialName("longitude") val longitude: Double,
    @SerialName("name") val name: String,
    @SerialName("country") val country: String? = null,
    @SerialName("country_code") val countryCode: String? = null
)