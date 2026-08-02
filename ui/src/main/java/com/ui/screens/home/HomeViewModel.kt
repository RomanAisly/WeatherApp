package com.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.CheckDataResult
import com.domain.CityItem
import com.domain.WeatherRepository
import com.ui.components.WindStatus
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.milliseconds

class HomeViewModel(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    private var searchJob: Job? = null

    fun showDialog() {
        _state.update { it.copy(showDialog = true) }
    }

    fun hideDialog() {
        _state.update {
            it.copy(showDialog = false, searchQuery = "", suggestedCities = emptyList())
        }
    }

    fun onSearchQueryChanged(query: String) {
        _state.update { it.copy(searchQuery = query) }
        searchJob?.cancel()

        if (query.isBlank()) {
            _state.update { it.copy(suggestedCities = emptyList()) }
            return
        }
        searchJob = viewModelScope.launch {
            delay(500.milliseconds)

            repository.searchCities(query).collect { result ->
                if (result is CheckDataResult.Success) {
                    _state.update { it.copy(suggestedCities = result.data) }
                }
            }
        }
    }

    fun updateCity(city: CityItem) {
        _state.update {
            it.copy(
                city = city.name,
                showDialog = false,
                searchQuery = "",
                suggestedCities = emptyList()
            )
        }
        loadWeather(city.latitude, city.longitude)
    }

    private fun loadWeather(lat: Double, lon: Double) {
        viewModelScope.launch {
            repository.getWeather(lat, lon).collect { result ->
                when (result) {
                    is CheckDataResult.Success -> {
                        val weather = result.data
                        _state.update {
                            it.copy(
                                gradus = weather.temperature.roundToInt().toString(),
                                wind = weather.windSpeed.roundToInt().toString(),
                                windStatus = WindStatus.fromSpeed(weather.windSpeed)
                            )
                        }
                    }

                    is CheckDataResult.Error -> {
                        _state.update { it.copy(gradus = "X", wind = "X") }
                    }
                }
            }
        }
    }
}