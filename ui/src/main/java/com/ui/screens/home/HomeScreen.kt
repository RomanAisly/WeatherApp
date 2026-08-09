package com.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ui.components.BaseAlertDialog
import com.ui.components.BaseIcon
import com.ui.components.BaseText
import com.ui.components.BaseTextButton
import com.ui.components.PrecipitationCard
import com.ui.components.UvCard
import com.ui.components.WeatherCard
import com.ui.components.WindCard
import com.ui.components.neonGlow
import com.ui.components.radialScreenBackground
import com.ui.theme.BaseTheme
import com.weatherapp.ui.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    paddingValues: PaddingValues,
    viewModel: HomeViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .radialScreenBackground(
                centerColor = BaseTheme.colors.bgCenter,
                haloColor = BaseTheme.colors.bgHalo,
                edgeColor = BaseTheme.colors.bgEdge,
            )
    ) {
        if (state.showDialog) {
            BaseAlertDialog(
                searchQuery = state.searchQuery,
                searchResults = state.suggestedCities,
                onQueryChange = { newText ->
                    viewModel.onSearchQueryChanged(newText)
                },
                onDismissRequest = {
                    viewModel.hideDialog()
                },
                onCityConfirmed = { selectedCity ->
                    viewModel.updateCity(selectedCity)
                }
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = paddingValues.calculateTopPadding() + 12.dp)
                .weight(0.55f)
        ) {
            Column(
                modifier = Modifier.align(Alignment.TopCenter),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    BaseTextButton(
                        text = stringResource(R.string.choose_your_city),
                        onClick = { viewModel.showDialog() })
                    BaseIcon(R.drawable.location, iconTint = BaseTheme.colors.iconTint)
                }
                BaseText(
                    state.city.ifEmpty { stringResource(R.string.your_city) },
                    textStyle = MaterialTheme.typography.headlineLarge
                )
            }

            BaseText(
                state.gradus + "℃",
                modifier = Modifier.align(Alignment.Center),
                textStyle = MaterialTheme.typography.displayLarge
            )
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 24.dp)
                    .neonGlow(
                        color = BaseTheme.colors.cardGlow,
                        shape = MaterialTheme.shapes.large,
                    )
            ) {
                BaseText(
                    state.currentTime,
                    textStyle = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(12.dp)
                )
            }

        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = paddingValues.calculateBottomPadding() + 16.dp)
                .weight(0.45f),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                WeatherCard(
                    weatherType = state.weatherType,
                    cloudCover = state.cloudCover,
                    timeDuration = state.weatherDuration,
                    modifier = Modifier.weight(1f)
                )
                WindCard(
                    windStrength = state.wind,
                    windStatus = state.windStatus,
                    timeDuration = state.windDuration,
                    modifier = Modifier.weight(1f)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                PrecipitationCard(
                    amount = state.precipAmount,
                    type = state.precipType,
                    timeDuration = state.precipDuration,
                    modifier = Modifier.weight(1f)
                )
                UvCard(
                    uvIndex = state.uvIndex,
                    uvStatus = state.uvStatus,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}