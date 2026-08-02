package com.domain

import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun searchCities(query: String): Flow<CheckDataResult<List<CityItem>, AppError>>
    suspend fun getWeather(lat: Double, lon: Double): Flow<CheckDataResult<Weather, AppError>>
}