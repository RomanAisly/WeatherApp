package com.data.mappers

import com.data.remote.dto.WeatherResponse
import com.domain.models.Weather
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
fun WeatherResponse.toDomain(): Weather {
    val current = this.current
    val hourly = this.hourly

    val currentHourTime = current.time.substringBeforeLast(":") + ":00"
    val currentIndex = hourly.time.indexOf(currentHourTime).takeIf { it != -1 } ?: 0

    return Weather(
        timezone = this.timezone,
        temperature = current.temperature,
        windSpeed = current.windSpeed,
        weatherCode = current.weatherCode,
        isDay = current.isDay == 1,
        precipitation = current.precipitation,
        cloudCover = current.cloudCover,
        hourlyWindSpeeds = hourly.windSpeed,
        hourlyWeatherCodes = hourly.weatherCode,
        currentIndex = currentIndex,
        uvIndex = current.uvIndex
    )
}

fun String.toFlagEmoji(): String {
    if (this.length != 2) return "🏳️"

    val countryCodeCaps = this.uppercase()

    val firstLetter = Character.codePointAt(countryCodeCaps, 0) - 0x41 + 0x1F1E6
    val secondLetter = Character.codePointAt(countryCodeCaps, 1) - 0x41 + 0x1F1E6

    return String(Character.toChars(firstLetter)) + String(Character.toChars(secondLetter))
}