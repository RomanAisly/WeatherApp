package com.domain.repositories

import com.domain.AppError
import com.domain.CheckDataResult
import com.domain.CityItem
import com.domain.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun searchCities(query: String): Flow<CheckDataResult<List<CityItem>, AppError>>
    suspend fun getWeather(lat: Double, lon: Double): Flow<CheckDataResult<Weather, AppError>>
}