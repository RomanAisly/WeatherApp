package com.ui.screens.globe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.CheckDataResult
import com.domain.CurrentCityManager
import com.domain.usecases.GetForecastUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class GlobeViewModel(
    private val getForecastUseCase: GetForecastUseCase,
    currentCityManager: CurrentCityManager
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val state: StateFlow<GlobeState> = currentCityManager.selectedCity
        .flatMapLatest { city ->
            if (city != null) {
                getForecastUseCase(city.latitude, city.longitude)
                    .map { result ->
                        if (result is CheckDataResult.Success) {
                            GlobeState(
                                hourlyForecasts = result.data.hourlyForecast,
                                dailyForecasts = result.data.dailyForecast
                            )
                        } else {
                            GlobeState()
                        }
                    }
            } else {
                flowOf(GlobeState())
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = GlobeState()
        )
}