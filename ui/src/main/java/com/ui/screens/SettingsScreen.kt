package com.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ui.components.BaseText
import com.ui.theme.BaseTheme

@Composable
fun SettingsScreen(paddingValues: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BaseTheme.colors.screenBack)
            .padding(paddingValues)
    ) {
        BaseText("Settings screen")
    }
}