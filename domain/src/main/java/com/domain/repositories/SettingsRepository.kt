package com.domain.repositories

import com.domain.AppLanguage
import com.domain.AppTheme
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val themeFlow: Flow<AppTheme>
    val languageFlow: Flow<AppLanguage>

    suspend fun saveTheme(theme: AppTheme)
    suspend fun saveLanguage(language: AppLanguage)
}