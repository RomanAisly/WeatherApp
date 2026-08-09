package com.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ui.navigation.BottomNavGraph
import com.ui.screens.settings.SettingsViewModel
import com.ui.theme.AppLanguageProvider
import com.ui.theme.WeatherTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val settingsViewModel: SettingsViewModel = koinViewModel()
            val currentTheme by settingsViewModel.themeState.collectAsStateWithLifecycle()
            val currentLanguage by settingsViewModel.languageState.collectAsStateWithLifecycle()
            val isDataLoaded = currentTheme != null && currentLanguage != null

            Crossfade(
                targetState = isDataLoaded,
                animationSpec = tween(durationMillis = 900)
            ) { isLoaded ->
                if (isLoaded) {
                    AppLanguageProvider(
                        setLanguage = currentLanguage!!,
                        onLanguageChange = { newLanguage ->
                            settingsViewModel.changeLanguage(newLanguage)
                        }) {
                        WeatherTheme(setTheme = currentTheme!!, onThemeChange = { newTheme ->
                            settingsViewModel.changeTheme(newTheme)
                        }) {
                            BottomNavGraph()
                        }
                    }
                } else {
                    Box(modifier = Modifier.fillMaxSize())
                }
            }
        }
    }
}