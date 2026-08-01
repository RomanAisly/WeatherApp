package com.ui.screens.home

import com.ui.components.WindStatus

data class HomeState(
    val city: String = "",
    val gradus: String = "--",
    val wind: String = "--",
    val windStatus: WindStatus = WindStatus.EASY,
    val showDialog: Boolean = false
)
