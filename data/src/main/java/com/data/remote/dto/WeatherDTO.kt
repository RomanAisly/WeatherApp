package com.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponse(
    @SerialName("timezone") val timezone: String,
    @SerialName("current") val current: CurrentWeatherDto,
    @SerialName("hourly") val hourly: HourlyWeatherDto
)

@Serializable
data class CurrentWeatherDto(
    @SerialName("time") val time: String,
    @SerialName("temperature_2m") val temperature: Double,
    @SerialName("wind_speed_10m") val windSpeed: Double,
    @SerialName("weather_code") val weatherCode: Int,
    @SerialName("is_day") val isDay: Int,
    @SerialName("precipitation") val precipitation: Double,
    @SerialName("cloud_cover") val cloudCover: Int,
    @SerialName("uv_index") val uvIndex: Double
)

@Serializable
data class HourlyWeatherDto(
    @SerialName("time") val time: List<String>,
    @SerialName("wind_speed_10m") val windSpeed: List<Double>,
    @SerialName("weather_code") val weatherCode: List<Int>,
    @SerialName("precipitation") val precipitation: List<Double>
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