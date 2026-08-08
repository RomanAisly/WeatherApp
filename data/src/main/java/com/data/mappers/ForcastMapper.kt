package com.data.mappers

import com.data.remote.dto.ForecastResponseDto
import com.domain.models.DailyForecast
import com.domain.models.ForecastDetails
import com.domain.models.HourlyForecast

fun ForecastResponseDto.toDomain(): ForecastDetails {
    val hourlyList = hourly.time.indices.map { i ->
        HourlyForecast(
            time = hourly.time[i],
            temperature = hourly.temperature[i],
            weatherCode = hourly.weatherCode[i],
            isDay = hourly.isDay[i] == 1,
            precipProbability = hourly.precipProb[i]
        )
    }

    val dailyList = daily.time.indices.map { i ->
        DailyForecast(
            date = daily.time[i],
            weatherCode = daily.weatherCode[i],
            maxTemp = daily.maxTemp[i],
            minTemp = daily.minTemp[i]
        )
    }

    return ForecastDetails(
        timezone = this.timezone,
        hourlyForecast = hourlyList,
        dailyForecast = dailyList
    )
}