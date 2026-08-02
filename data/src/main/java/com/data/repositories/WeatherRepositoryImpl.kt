package com.data.repositories

import android.util.Log
import com.data.remote.WeatherProvider
import com.domain.AppError
import com.domain.CheckDataResult
import com.domain.CityItem
import com.domain.Weather
import com.domain.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WeatherRepositoryImpl(
    private val weatherProvider: WeatherProvider
) : WeatherRepository {

    override suspend fun searchCities(query: String): Flow<CheckDataResult<List<CityItem>, AppError>> =
        flow {
            try {
                val response = weatherProvider.searchCities(query)

                val cities = response.results?.map {
                    CityItem(
                        name = it.name,
                        country = it.country ?: "Unknown",
                        latitude = it.latitude,
                        longitude = it.longitude
                    )
                } ?: emptyList()

                emit(CheckDataResult.Success(cities))
            } catch (e: Exception) {
                emit(CheckDataResult.Error(handleError(e)))
            }
        }

    override suspend fun getWeather(
        lat: Double,
        lon: Double
    ): Flow<CheckDataResult<Weather, AppError>> = flow {
        try {
            val response = weatherProvider.getCurrentWeather(lat, lon)
            val domainWeather = Weather(
                temperature = response.current.temperature,
                windSpeed = response.current.windSpeed
            )
            emit(CheckDataResult.Success(domainWeather))
        } catch (e: Exception) {
            emit(CheckDataResult.Error(handleError(e)))
        }
    }

    private fun handleError(e: Exception): AppError {
        Log.d("MyLog", "Weather Error: $e")
        return when (e) {
            is java.net.UnknownHostException -> AppError.NO_INTERNET
            is io.ktor.client.plugins.HttpRequestTimeoutException -> AppError.TIMEOUT
            is io.ktor.client.plugins.ServerResponseException -> AppError.SERVER_ERROR
            else -> AppError.UNKNOWN
        }
    }
}