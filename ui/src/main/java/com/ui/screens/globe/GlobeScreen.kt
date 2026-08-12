package com.ui.screens.globe

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.domain.models.DailyForecast
import com.domain.models.HourlyForecast
import com.ui.components.AnimLoad
import com.ui.components.BaseCard
import com.ui.components.BaseIcon
import com.ui.components.BaseText
import com.ui.components.PrecipitationType
import com.ui.components.TemperatureBar
import com.ui.components.WeatherType
import com.ui.components.WindStatus
import com.ui.components.toDayNameRes
import com.ui.theme.BaseTheme
import com.weatherapp.ui.R
import org.koin.androidx.compose.koinViewModel
import kotlin.math.roundToInt

@Composable
fun GlobeScreen(
    paddingValues: PaddingValues,
    viewModel: GlobeViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current

    val displayHourly = state.hourlyForecasts.ifEmpty { List(24) { null } }
    val displayDaily = state.dailyForecasts.ifEmpty { List(10) { null } }
    val layoutDirection = LocalLayoutDirection.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.refreshForecast()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BaseTheme.colors.scaffoldBack)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = paddingValues.calculateTopPadding() + 16.dp
                ),
            contentPadding = PaddingValues(
                start = paddingValues.calculateStartPadding(
                    LayoutDirection.Ltr
                ) + 16.dp, end = paddingValues.calculateEndPadding(LayoutDirection.Rtl) + 16.dp
            ),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(displayHourly) { hourlyItem ->
                Column(
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    HourlyWindCard(item = hourlyItem)
                    HourlyPrecipCard(item = hourlyItem)
                }
            }
        }
        BaseCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    bottom = paddingValues.calculateBottomPadding() + 16.dp,
                    start = paddingValues.calculateStartPadding(layoutDirection) + 16.dp,
                    end = paddingValues.calculateEndPadding(layoutDirection) + 16.dp
                )
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                BaseText(
                    text = stringResource(R.string._10_day_forecast),
                    textStyle = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                displayDaily.forEach { dailyItem ->
                    DailyCard(
                        item = dailyItem
                    )
                }
            }
        }
    }
}

@Composable
fun HourlyBaseCard(
    timeText: String,
    valueText: String,
    temperatureText: String,
    lottieRes: Int?,
    lottieTint: Color?,
    modifier: Modifier = Modifier
) {
    BaseCard(modifier = modifier.width(70.dp)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            BaseText(text = timeText)

            if (lottieRes != null) {
                AnimLoad(
                    resId = lottieRes,
                    tintColor = lottieTint,
                    modifier = Modifier.size(28.dp)
                )
            } else {
                Box(modifier = Modifier.size(28.dp))
            }

            BaseText(
                text = valueText,
                textStyle = MaterialTheme.typography.bodySmall,
            )

            BaseText(
                text = temperatureText,
                textStyle = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
private fun HourlyWindCard(item: HourlyForecast?) {
    val windStatus = if (item != null) WindStatus.fromSpeed(item.windSpeed) else WindStatus.UNKNOWN

    val lottieTint = if (windStatus == WindStatus.LIGHT) BaseTheme.colors.text else null

    HourlyBaseCard(
        timeText = item?.let { if (it.isNow) stringResource(R.string.now) else it.time } ?: "--",
        valueText = item?.let { "${it.windSpeed.roundToInt()}" } ?: "--",
        temperatureText = item?.let { "${it.temperature.roundToInt()}°" } ?: "--°",
        lottieRes = windStatus.lottieRes,
        lottieTint = lottieTint
    )
}

@Composable
private fun HourlyPrecipCard(item: HourlyForecast?) {
    val precipType =
        if (item != null) PrecipitationType.fromWmoCode(item.weatherCode) else PrecipitationType.NONE

    val lottieTint = if (precipType in listOf(PrecipitationType.RAIN, PrecipitationType.DRIZZLE)) {
        BaseTheme.colors.rain
    } else null

    HourlyBaseCard(
        timeText = item?.let { if (it.isNow) stringResource(R.string.now) else it.time } ?: "--",
        valueText = item?.let { "${it.precipitation} mm" } ?: "-- mm",
        temperatureText = item?.let { "${it.temperature.roundToInt()}°" } ?: "--°",
        lottieRes = precipType.lottieRes,
        lottieTint = lottieTint
    )
}

@Composable
private fun DailyCard(
    item: DailyForecast?
) {
    val weatherType = if (item != null) WeatherType.fromWmoCode(
        item.weatherCode,
        isDay = true
    ) else WeatherType.UNKNOWN

    val precipType = if (item != null) PrecipitationType.fromWmoCode(
        item.weatherCode
    ) else PrecipitationType.NONE

    val dayText = if (item != null) {
        if (item.isToday) stringResource(R.string.today) else item.dayOfWeek.toDayNameRes()
    } else {
        stringResource(R.string.unknown)
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BaseText(
            text = dayText,
            modifier = Modifier.weight(1f),
            textStyle = MaterialTheme.typography.titleSmall,
            maxLines = 1
        )
        Row(
            modifier = Modifier
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (weatherType.staticIconRes != null) {
                BaseIcon(weatherType.staticIconRes)
            } else {
                Box(modifier = Modifier.size(32.dp))
            }

            BaseIcon(precipType.staticIconRes)
        }
        Row(
            modifier = Modifier
                .weight(1.5f),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BaseText(
                text = item?.let { "${it.minTemp.roundToInt()}°" } ?: "--°",
                textStyle = MaterialTheme.typography.bodyLarge
            )
            TemperatureBar(
                minTemp = item?.minTemp,
                maxTemp = item?.maxTemp,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            )
            BaseText(
                text = item?.let { "${it.maxTemp.roundToInt()}°" } ?: "--°",
                textStyle = MaterialTheme.typography.titleMedium
            )
        }
    }
}