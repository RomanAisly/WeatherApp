package com.data.mapers

import com.data.remote.CurrentWeatherDto
import com.domain.Weather

fun CurrentWeatherDto.toDomain(): Weather {
    return Weather(
        temperature = this.temperature,
        windSpeed = this.windSpeed,
    )
}

fun String.toFlagEmoji(): String {
    if (this.length != 2) return "🏳️"

    val countryCodeCaps = this.uppercase()

    val firstLetter = Character.codePointAt(countryCodeCaps, 0) - 0x41 + 0x1F1E6
    val secondLetter = Character.codePointAt(countryCodeCaps, 1) - 0x41 + 0x1F1E6

    return String(Character.toChars(firstLetter)) + String(Character.toChars(secondLetter))
}