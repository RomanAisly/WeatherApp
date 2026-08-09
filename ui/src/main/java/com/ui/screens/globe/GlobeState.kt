package com.ui.screens.globe

import com.domain.models.DailyForecast
import com.domain.models.HourlyForecast

data class GlobeState(
    val hourlyForecasts: List<HourlyForecast> = emptyList(),
    val dailyForecasts: List<DailyForecast> = emptyList()
)