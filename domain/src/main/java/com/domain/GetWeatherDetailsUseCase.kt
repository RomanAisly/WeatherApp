package com.domain

import com.domain.repositories.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetWeatherDetailsUseCase(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(
        lat: Double,
        lon: Double
    ): Flow<CheckDataResult<WeatherDetails, AppError>> {
        return repository.getWeather(lat, lon).map { result ->
            when (result) {
                is CheckDataResult.Success -> {
                    val weather = result.data
                    val details = calculateDurations(weather)
                    CheckDataResult.Success(details)
                }

                is CheckDataResult.Error -> result
            }
        }
    }

    private fun calculateDurations(weather: Weather): WeatherDetails {
        fun countHours(condition: (Int) -> Boolean): String {
            var count = 0
            for (i in weather.currentIndex until weather.hourlyWeatherCodes.size) {
                if (condition(i)) count++ else break
            }
            return if (count >= 24) ">24 h" else "$count h"
        }

        fun getWindCategory(speed: Double) = when {
            speed < 15.0 -> 0
            speed < 35.0 -> 1
            else -> 2
        }

        fun getPrecipCategory(code: Int) = when (code) {
            51, 53, 55 -> 1 // Drizzle
            56, 57, 66, 67 -> 2 // Freezing
            61, 63, 65, 80, 81, 82 -> 3 // Rain
            71, 73, 75, 77, 85, 86 -> 4 // Snow
            95, 96, 99 -> 5 // Storm
            else -> 0 // None
        }

        val currentWindCat = getWindCategory(weather.windSpeed)
        val currentPrecipCat = getPrecipCategory(weather.weatherCode)

        val windDur =
            countHours { i -> getWindCategory(weather.hourlyWindSpeeds[i]) == currentWindCat }
        val precipDur =
            countHours { i -> getPrecipCategory(weather.hourlyWeatherCodes[i]) == currentPrecipCat }
        val weatherDur = countHours { i -> weather.hourlyWeatherCodes[i] == weather.weatherCode }

        return WeatherDetails(
            temperature = weather.temperature,
            windSpeed = weather.windSpeed,
            weatherCode = weather.weatherCode,
            isDay = weather.isDay,
            precipitation = weather.precipitation,
            cloudCover = weather.cloudCover,
            windDuration = windDur,
            precipDuration = precipDur,
            weatherDuration = weatherDur
        )
    }
}