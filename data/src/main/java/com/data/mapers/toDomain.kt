package com.data.mapers

import com.data.remote.CurrentWeatherDto
import com.domain.Weather

fun CurrentWeatherDto.toDomain(cityName: String): Weather {
    return Weather(
        temperature = this.temperature,
        windSpeed = this.windSpeed,
    )
}