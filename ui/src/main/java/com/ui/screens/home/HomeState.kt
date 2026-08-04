package com.ui.screens.home

import com.domain.CityItem
import com.ui.components.WeatherType
import com.ui.components.WindStatus

data class HomeState(
    val city: String = "",
    val gradus: String = "--",
    val wind: String = "--",
    val windStatus: WindStatus = WindStatus.EASY,
    val weatherType: WeatherType = WeatherType.OVERCAST,
    val showDialog: Boolean = false,
    val searchQuery: String = "",
    val suggestedCities: List<CityItem> = emptyList()
)
