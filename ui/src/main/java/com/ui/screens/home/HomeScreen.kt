package com.ui.screens.home

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ui.components.BaseIcon
import com.ui.components.BaseText
import com.ui.components.BaseTextButton
import com.ui.components.CitySearchOverlay
import com.ui.components.FadeWrapper
import com.ui.components.GpsWarningDialog
import com.ui.components.LayoutMode
import com.ui.components.PrecipitationCard
import com.ui.components.ScreenLoader
import com.ui.components.SnackBarFlow
import com.ui.components.UvCard
import com.ui.components.WeatherCard
import com.ui.components.WindCard
import com.ui.components.getMessageRes
import com.ui.components.neonGlow
import com.ui.components.radialScreenBackground
import com.ui.theme.BaseTheme
import com.weatherapp.ui.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    paddingValues: PaddingValues,
    layoutMode: LayoutMode,
    viewModel: HomeViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val isGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

        viewModel.onPermissionResult(isGranted)
    }

    LaunchedEffect(state.askForLocationPermission) {
        if (state.askForLocationPermission) {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.onResumeApp()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .radialScreenBackground(
                centerColor = BaseTheme.colors.bgCenter,
                haloColor = BaseTheme.colors.bgHalo,
                edgeColor = BaseTheme.colors.bgEdge,
            )
    ) {
        if (state.showGpsWarning) {
            GpsWarningDialog(
                onGoToSettings = { viewModel.dismissGpsWarning(goToSettings = true) },
                onSearchManually = { viewModel.dismissGpsWarning(goToSettings = false) }
            )
        }

        if (state.isLocating) {
            ScreenLoader()
        }

        if (state.showDialog) {
            CitySearchOverlay(
                searchQuery = state.searchQuery,
                searchResults = state.suggestedCities,
                layoutMode = layoutMode,
                onQueryChange = { newText ->
                    viewModel.onSearchQueryChanged(newText)
                },
                onDismissRequest = {
                    viewModel.closeCitySearchOverlay()
                },
                onCityConfirmed = { selectedCity ->
                    viewModel.updateCity(selectedCity)
                }
            )
        }

        if (layoutMode == LayoutMode.LANDSCAPE_PHONE) {
            Row(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                HomeHeader(
                    state = state,
                    onShowDialog = { viewModel.showCitySearchOverlay() },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(
                            top = paddingValues.calculateTopPadding(),
                            start = paddingValues.calculateStartPadding(LayoutDirection.Rtl)
                        )
                )
                HomeWidgets(
                    state = state,
                    isLandscape = true,
                    topPadding = paddingValues.calculateTopPadding(),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(
                            end = paddingValues.calculateEndPadding(LayoutDirection.Ltr) + 8.dp
                        )
                )
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                HomeHeader(
                    state = state,
                    onShowDialog = { viewModel.showCitySearchOverlay() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = paddingValues.calculateTopPadding() + 12.dp)
                        .weight(0.55f)
                )
                HomeWidgets(
                    state = state,
                    isLandscape = false,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = paddingValues.calculateBottomPadding() + 16.dp)
                        .weight(0.45f)
                )
            }
        }
        SnackBarFlow(
            snackFlow = viewModel.snack,
            messageRes = { it.getMessageRes() },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = paddingValues.calculateBottomPadding() + 16.dp)
        )
    }
}

@Composable
private fun HomeHeader(
    state: HomeState,
    onShowDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
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
                    onClick = onShowDialog
                )
                BaseIcon(R.drawable.location, iconTint = BaseTheme.colors.iconTint)
            }
            FadeWrapper(
                targetState = state.city.ifEmpty { stringResource(R.string.your_city) }
            ) { animCity ->
                BaseText(
                    text = animCity,
                    textStyle = MaterialTheme.typography.headlineLarge
                )
            }
        }
        FadeWrapper(
            targetState = state.gradus + "℃",
            modifier = Modifier.align(Alignment.Center)
        ) { animGradus ->
            BaseText(
                text = animGradus,
                textStyle = MaterialTheme.typography.displayLarge
            )
        }
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
}

@Composable
private fun HomeWidgets(
    state: HomeState,
    isLandscape: Boolean,
    modifier: Modifier = Modifier,
    topPadding: Dp = 0.dp
) {
    if (isLandscape) {
        Column(
            modifier = modifier
                .verticalScroll(rememberScrollState())
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            if (topPadding > 0.dp) {
                Spacer(modifier = Modifier.height(topPadding))
            }
            WeatherCard(state.weatherType, state.cloudCover, state.weatherDuration)
            WindCard(state.wind, state.windStatus, state.windDuration)
            PrecipitationCard(state.precipAmount, state.precipType, state.precipDuration)
            UvCard(state.uvIndex, state.uvStatus)
        }
    } else {
        Column(
            modifier = modifier,
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