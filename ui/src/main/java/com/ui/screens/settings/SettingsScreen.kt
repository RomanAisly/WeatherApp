package com.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.domain.AppLanguage
import com.domain.AppTheme
import com.ui.components.BaseIcon
import com.ui.components.BaseText
import com.ui.components.getIconRes
import com.ui.components.getTitleRes
import com.ui.theme.BaseTheme
import com.ui.theme.LocalLanguageChangeHandler
import com.ui.theme.LocalSetLanguage
import com.ui.theme.LocalSetTheme
import com.ui.theme.LocalThemeChangeHandler
import com.weatherapp.ui.R

@Composable
fun SettingsScreen(paddingValues: PaddingValues) {

    val currentTheme = LocalSetTheme.current
    val onThemeChange = LocalThemeChangeHandler.current
    val currentLanguage = LocalSetLanguage.current
    val onLanguageChange = LocalLanguageChangeHandler.current
    val layoutDirection = LocalLayoutDirection.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BaseTheme.colors.settScreenBack)
            .padding(
                top = paddingValues.calculateTopPadding() + 16.dp,
                bottom = paddingValues.calculateBottomPadding() + 26.dp,
                start = paddingValues.calculateStartPadding(layoutDirection) + 12.dp,
                end = paddingValues.calculateEndPadding(layoutDirection) + 12.dp
            )
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        BaseText(stringResource(R.string.theme), textStyle = MaterialTheme.typography.headlineLarge)

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AppTheme.entries.forEach { option ->
                ThemeItem(
                    option = option,
                    isSelected = option == currentTheme,
                    onClick = onThemeChange
                )
            }
        }

        BaseText(
            stringResource(R.string.language),
            textStyle = MaterialTheme.typography.headlineLarge
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AppLanguage.entries.forEach { language ->
                LanguageItem(
                    option = language,
                    isSelected = currentLanguage == language,
                    onClick = { onLanguageChange(language) }
                )
            }
        }
    }
}

@Composable
private fun ThemeItem(option: AppTheme, isSelected: Boolean, onClick: (AppTheme) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(MaterialTheme.shapes.medium)
            .border(
                shape = MaterialTheme.shapes.medium,
                width = 0.5.dp,
                color = BaseTheme.colors.text
            )

            .clickable(role = Role.Switch, onClick = { onClick(option) })
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        BaseIcon(option.getIconRes())
        BaseText(
            stringResource(option.getTitleRes()),
            textStyle = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = isSelected,
            onCheckedChange = { isChecked ->
                if (isChecked) onClick(option)
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = BaseTheme.colors.bottomBarEnd,
                checkedTrackColor = BaseTheme.colors.bottomBarStart
            )
        )
    }
}

@Composable
private fun LanguageItem(option: AppLanguage, isSelected: Boolean, onClick: (AppLanguage) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(MaterialTheme.shapes.medium)
            .border(
                shape = MaterialTheme.shapes.medium,
                width = 0.5.dp,
                color = BaseTheme.colors.text
            )

            .clickable(role = Role.Switch, onClick = { onClick(option) })
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        BaseText(
            stringResource(option.getTitleRes()),
            textStyle = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = isSelected,
            onCheckedChange = { isChecked ->
                if (isChecked) onClick(option)
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = BaseTheme.colors.bottomBarEnd,
                checkedTrackColor = BaseTheme.colors.bottomBarStart
            )
        )
    }
}