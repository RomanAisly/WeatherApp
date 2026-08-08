package com.domain.usecases

import com.domain.AppError
import com.domain.CheckDataResult
import com.domain.models.Weather
import com.domain.models.WeatherDetails
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
            var count = 1
            for (i in (weather.currentIndex + 1) until weather.hourlyWeatherCodes.size) {
                if (condition(i)) count++ else break
            }
            return if (count >= 24) ">24" else count.toString()
        }

        fun getWindCategory(speed: Double) = when {
            speed < 5.0 -> 0   // CALM
            speed < 10.0 -> 1  // LIGHT
            speed < 15.0 -> 2  // GENTLE
            speed < 25.0 -> 3  // MODERATE
            else -> 4          // STRONG
        }

        fun getPrecipCategory(code: Int) = when (code) {
            51, 53, 55 -> 1 // Drizzle
            56, 57, 66, 67 -> 2 // Freezing
            61, 63, 65, 80, 81, 82 -> 3 // Rain
            71, 73, 75, 77, 85, 86 -> 4 // Snow
            95, 96, 99 -> 5 // Storm
            else -> 0 // None
        }

        fun getWeatherCategory(code: Int) = when (code) {
            0 -> 0 // Clear
            1 -> 1 // Mainly clear
            2 -> 2 // Partly cloudy
            3 -> 3 // Overcast
            45, 48 -> 4 // Fog
            in 51..99 -> 3
            else -> -1
        }

        val currentWindCat = getWindCategory(weather.windSpeed)
        val currentPrecipCat = getPrecipCategory(weather.weatherCode)
        val currentWeatherCat = getWeatherCategory(weather.weatherCode)

        val windDur =
            countHours { i -> getWindCategory(weather.hourlyWindSpeeds[i]) == currentWindCat }
        val precipDur =
            countHours { i -> getPrecipCategory(weather.hourlyWeatherCodes[i]) == currentPrecipCat }
        val weatherDur =
            countHours { i -> getWeatherCategory(weather.hourlyWeatherCodes[i]) == currentWeatherCat }

        return WeatherDetails(
            timezone = weather.timezone,
            temperature = weather.temperature,
            windSpeed = weather.windSpeed,
            weatherCode = weather.weatherCode,
            isDay = weather.isDay,
            precipitation = weather.precipitation,
            cloudCover = weather.cloudCover,
            windDuration = windDur,
            precipDuration = precipDur,
            weatherDuration = weatherDur,
            uvIndex = weather.uvIndex
        )
    }
}