package com.domain

import com.domain.models.CityItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CurrentCityManager {
    private val _selectedCity = MutableStateFlow<CityItem?>(null)
    val selectedCity = _selectedCity.asStateFlow()

    fun setCity(city: CityItem) {
        _selectedCity.value = city
    }
}