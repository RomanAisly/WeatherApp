package com.ui.screens.globe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.CheckDataResult
import com.domain.CurrentCityManager
import com.domain.usecases.GetForecastUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.minutes

class GlobeViewModel(
    private val getForecastUseCase: GetForecastUseCase,
    private val currentCityManager: CurrentCityManager
) : ViewModel() {

    private val _state = MutableStateFlow(GlobeState())
    val state = _state.asStateFlow()

    private var lastUpdateTime: Long = 0
    private val updateIntervalMillis = 10.minutes.inWholeMilliseconds

    init {
        observeCityChanges()
    }

    fun refreshForecast(forceRefresh: Boolean = false) {
        val currentCity = currentCityManager.selectedCity.value ?: return
        val currentTime = System.currentTimeMillis()
        val timeSinceLastUpdate = currentTime - lastUpdateTime

        if (!forceRefresh && timeSinceLastUpdate < updateIntervalMillis) {
            return
        }
        viewModelScope.launch {
            getForecastUseCase(currentCity.latitude, currentCity.longitude).collect { result ->
                if (result is CheckDataResult.Success) {
                    lastUpdateTime = System.currentTimeMillis()
                    _state.update {
                        it.copy(
                            hourlyForecasts = result.data.hourlyForecast,
                            dailyForecasts = result.data.dailyForecast
                        )
                    }
                }
            }
        }
    }

    private fun observeCityChanges() {
        viewModelScope.launch {
            currentCityManager.selectedCity
                .filterNotNull()
                .collectLatest { _ ->
                    refreshForecast(forceRefresh = true)
                }
        }
    }
}