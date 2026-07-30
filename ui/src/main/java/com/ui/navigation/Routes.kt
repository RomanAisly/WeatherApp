package com.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Routes : NavKey {
    @Serializable
    data object Home : Routes

    @Serializable
    data object Globe : Routes

    @Serializable
    data object Settings : Routes
}