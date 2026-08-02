package com.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.AppLanguage
import com.domain.AppTheme
import com.domain.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(private val settingsRepository: SettingsRepository) : ViewModel() {

    val themeState: StateFlow<AppTheme> = settingsRepository.themeFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AppTheme.SYSTEM
    )

    val languageState: StateFlow<AppLanguage> = settingsRepository.languageFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AppLanguage.ENGLISH
    )


    fun changeTheme(newTheme: AppTheme) {
        viewModelScope.launch {
            settingsRepository.saveTheme(newTheme)
        }
    }

    fun changeLanguage(newLanguage: AppLanguage) {
        viewModelScope.launch {
            settingsRepository.saveLanguage(newLanguage)
        }
    }
}