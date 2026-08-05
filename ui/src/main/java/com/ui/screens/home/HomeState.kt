package com.ui.screens.home

import com.domain.CityItem
import com.ui.components.PrecipitationType
import com.ui.components.WeatherType
import com.ui.components.WindStatus

data class HomeState(
    val city: String = "",
    val gradus: String = "--",
    val wind: String = "--",
    val windStatus: WindStatus = WindStatus.UNKNOWN,
    val weatherType: WeatherType = WeatherType.UNKNOWN,
    val precipType: PrecipitationType = PrecipitationType.NONE,
    val showDialog: Boolean = false,
    val searchQuery: String = "",
    val suggestedCities: List<CityItem> = emptyList(),
    val precipAmount: String = "-- mm",
    val cloudCover: String = "-- %",
    val windDuration: String = "--",
    val precipDuration: String = "--",
    val weatherDuration: String = "--"
)
