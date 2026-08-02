package com.ui.theme

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import com.domain.AppLanguage
import java.util.Locale

val LocalSetLanguage = compositionLocalOf { AppLanguage.ENGLISH }
val LocalLanguageChangeHandler = compositionLocalOf<(AppLanguage) -> Unit> { {} }

@Composable
fun AppLanguageProvider(
    setLanguage: AppLanguage,
    onLanguageChange: (AppLanguage) -> Unit,
    content: @Composable () -> Unit
) {
    val locale = Locale.forLanguageTag(setLanguage.localeCode)
    Locale.setDefault(locale)

    val configuration = Configuration(LocalConfiguration.current)
    configuration.setLocale(locale)

    val newConfigurationContext = LocalContext.current.createConfigurationContext(configuration)

    CompositionLocalProvider(
        LocalContext provides newConfigurationContext,
        LocalConfiguration provides configuration,
        LocalSetLanguage provides setLanguage,
        LocalLanguageChangeHandler provides onLanguageChange,
        content = content
    )
}