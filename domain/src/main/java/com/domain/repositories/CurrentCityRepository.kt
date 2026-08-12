package com.domain.repositories

import com.domain.models.CityItem
import kotlinx.coroutines.flow.Flow

interface CurrentCityRepository {
    val selectedCity: Flow<CityItem?>
    suspend fun setCity(city: CityItem)
}