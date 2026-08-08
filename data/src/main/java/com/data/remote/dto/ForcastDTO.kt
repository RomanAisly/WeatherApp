package com.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ForecastResponseDto(
    @SerialName("timezone") val timezone: String,
    @SerialName("hourly") val hourly: ForecastHourlyDto,
    @SerialName("daily") val daily: ForecastDailyDto
)

@Serializable
data class ForecastHourlyDto(
    @SerialName("time") val time: List<String>,
    @SerialName("temperature_2m") val temperature: List<Double>,
    @SerialName("precipitation_probability") val precipProb: List<Int>,
    @SerialName("weather_code") val weatherCode: List<Int>,
    @SerialName("is_day") val isDay: List<Int>
)

@Serializable
data class ForecastDailyDto(
    @SerialName("time") val time: List<String>,
    @SerialName("weather_code") val weatherCode: List<Int>,
    @SerialName("temperature_2m_max") val maxTemp: List<Double>,
    @SerialName("temperature_2m_min") val minTemp: List<Double>
)