package com.domain

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val themeFlow: Flow<AppTheme>
    val languageFlow: Flow<AppLanguage>

    suspend fun saveTheme(theme: AppTheme)
    suspend fun saveLanguage(language: AppLanguage)
}