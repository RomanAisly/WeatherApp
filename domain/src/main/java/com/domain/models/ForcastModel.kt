package com.domain.models

data class ForecastDetails(
    val timezone: String,
    val hourlyForecast: List<HourlyForecast>,
    val dailyForecast: List<DailyForecast>
)

data class HourlyForecast(
    val time: String,
    val isNow: Boolean = false,
    val temperature: Double,
    val precipProbability: Int,
    val weatherCode: Int,
    val isDay: Boolean
)

data class DailyForecast(
    val date: String,
    val dayOfWeek: Int = 1,
    val isToday: Boolean = false,
    val weatherCode: Int,
    val minTemp: Double,
    val maxTemp: Double
)