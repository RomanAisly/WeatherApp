package com.ui.components


import com.ui.screens.home.HomeViewModel
import com.ui.screens.settings.SettingsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val previewModule = module {
    viewModelOf(::HomeViewModel)
}
val viewModelsModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::SettingsViewModel)
}