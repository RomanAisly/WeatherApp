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
import com.ui.components.WindCard
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
    val topPadding = paddingValues.calculateTopPadding() + 30.dp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .radialScreenBackground(
                centerColor = BaseTheme.colors.bgCenter,
                haloColor = BaseTheme.colors.bgHalo,
                edgeColor = BaseTheme.colors.bgEdge,
                topOffset = topPadding
            )
            .padding(top = topPadding)
    ) {
        if (state.showDialog) {
            BaseAlertDialog(
                onDismissRequest = {
                    viewModel.hideDialog()
                },
                onCityConfirmed = { cityName ->
                    viewModel.updateCity(cityName)
                }
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.6f)
        ) {
            Column(
                modifier = Modifier.align(Alignment.TopCenter),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    BaseTextButton(
                        text = stringResource(R.string.choose_your_city),
                        onClick = { viewModel.showDialog() })
                    BaseIcon(R.drawable.location)
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
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .weight(0.4f),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                WindCard(
                    windStrength = state.wind,
                    windStatus = state.windStatus, modifier = Modifier.weight(1f)
                )
//                WindCard("17", modifier = Modifier.weight(1f))
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
//                WindCard("12", modifier = Modifier.weight(1f))
//                WindCard("17", modifier = Modifier.weight(1f))
            }
        }
    }
}