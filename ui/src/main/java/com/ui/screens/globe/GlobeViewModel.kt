package com.ui.screens.globe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.CheckDataResult
import com.domain.CurrentCityManager
import com.domain.usecases.GetForecastUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GlobeViewModel(
    private val getForecastUseCase: GetForecastUseCase,
    private val currentCityManager: CurrentCityManager
) : ViewModel() {

    private val _state = MutableStateFlow(GlobeState())
    val state = _state.asStateFlow()

    init {
        observeCityChanges()
    }

    private fun observeCityChanges() {
        viewModelScope.launch {
            currentCityManager.selectedCity.collectLatest { city ->
                if (city != null) {
                    _state.update { it.copy(isLoading = true) }
                    loadForecast(city.latitude, city.longitude)
                } else {
                    _state.update { GlobeState() }
                }
            }
        }
    }

    private suspend fun loadForecast(lat: Double, lon: Double) {
        getForecastUseCase(lat, lon).collect { result ->
            if (result is CheckDataResult.Success) {
                _state.update {
                    it.copy(
                        hourlyForecasts = result.data.hourlyForecast,
                        dailyForecasts = result.data.dailyForecast,
                        isLoading = false
                    )
                }
            } else {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }
}