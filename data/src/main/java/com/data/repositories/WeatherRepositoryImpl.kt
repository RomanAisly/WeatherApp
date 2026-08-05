package com.data.repositories

import android.util.Log
import com.data.mapers.toDomain
import com.data.mapers.toFlagEmoji
import com.data.remote.WeatherProvider
import com.domain.AppError
import com.domain.CheckDataResult
import com.domain.CityItem
import com.domain.Weather
import com.domain.repositories.WeatherRepository
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.UnknownHostException

class WeatherRepositoryImpl(
    private val weatherProvider: WeatherProvider
) : WeatherRepository {
    override suspend fun searchCities(query: String): Flow<CheckDataResult<List<CityItem>, AppError>> =
        flow {
            try {
                val response = weatherProvider.searchCities(query)
                val cities = response.results?.map {
                    CityItem(
                        id = it.id,
                        name = it.name,
                        country = it.country ?: "Unknown",
                        flagEmoji = it.countryCode?.toFlagEmoji() ?: "🏳️",
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

            emit(CheckDataResult.Success(response.toDomain()))
        } catch (e: Exception) {
            emit(CheckDataResult.Error(handleError(e)))
        }
    }

    private fun handleError(e: Exception): AppError {
        Log.e("WeatherRepository", "Network Error: ", e)
        return when (e) {
            is UnresolvedAddressException, is UnknownHostException -> AppError.NO_INTERNET
            is HttpRequestTimeoutException, is ConnectTimeoutException -> AppError.TIMEOUT
            is ClientRequestException -> {
                when (e.response.status.value) {
                    401, 403 -> AppError.UNAUTHORIZED
                    404 -> AppError.NOT_FOUND
                    429 -> AppError.SERVER_ERROR
                    else -> AppError.UNKNOWN
                }
            }

            is ServerResponseException -> AppError.SERVER_ERROR
            else -> AppError.UNKNOWN
        }
    }
}