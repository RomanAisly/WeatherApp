package com.data.repositories

import android.util.Log
import com.data.mapers.toDomain
import com.data.remote.WeatherProvider
import com.data.remote.WeatherRequest
import com.domain.AppError
import com.domain.CheckDataResult
import com.domain.Weather
import com.domain.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

// data/repositories/WeatherRepositoryImpl.kt
class WeatherRepositoryImpl(
    private val weatherProvider: WeatherProvider
) : WeatherRepository {

    override suspend fun getWeather(
        lat: Double,
        lon: Double
    ): Flow<CheckDataResult<Weather, AppError>> = flow {
        try {
            val request = WeatherRequest(lat, lon)
            // 1. Получаем DTO из сети
            val response = weatherProvider.getCurrentWeather(request)

            // 2. Мапим DTO в чистый Domain объект
            val domainWeather = response.current.toDomain()

            // 3. Отправляем успех в Flow
            emit(CheckDataResult.Success(domainWeather))

        } catch (e: Exception) {
            Log.d("MyLog", "getWeather: $e")
            // Обрабатываем ошибки Ktor и переводим их в ваши AppError
            val appError = when (e) {
                is java.net.UnknownHostException -> AppError.NO_INTERNET
                is io.ktor.client.plugins.HttpRequestTimeoutException -> AppError.TIMEOUT
                is io.ktor.client.plugins.ServerResponseException -> AppError.SERVER_ERROR
                else -> AppError.UNKNOWN
            }
            emit(CheckDataResult.Error(appError))
        }
    }
}