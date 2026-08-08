package com.ui.screens.globe

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.domain.models.DailyForecast
import com.domain.models.HourlyForecast
import com.ui.components.AnimLoad
import com.ui.components.BaseCard
import com.ui.components.BaseText
import com.ui.components.TemperatureBar
import com.ui.components.WeatherType
import com.weatherapp.ui.R
import org.koin.androidx.compose.koinViewModel
import kotlin.math.roundToInt

@Composable
fun GlobeScreen(
    paddingValues: PaddingValues,
    viewModel: GlobeViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val layoutDirection = LocalLayoutDirection.current

    val displayHourly = state.hourlyForecasts.ifEmpty { List(24) { null } }
    val displayDaily = state.dailyForecasts.ifEmpty { List(10) { null } }

    val weeklyMin = state.dailyForecasts.minOfOrNull { it.minTemp } ?: 0.0
    val weeklyMax = state.dailyForecasts.maxOfOrNull { it.maxTemp } ?: 100.0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = paddingValues.calculateTopPadding() + 20.dp,
                bottom = paddingValues.calculateBottomPadding(),
                start = paddingValues.calculateStartPadding(layoutDirection),
                end = paddingValues.calculateEndPadding(layoutDirection)
            )
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(displayHourly) { hourlyItem ->
                HourlyCard(item = hourlyItem)
            }
        }
        BaseCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                BaseText(
                    text = "10-DAY FORECAST",
                    textStyle = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                displayDaily.forEach { dailyItem ->
                    DailyRow(
                        item = dailyItem,
                        weeklyMin = weeklyMin,
                        weeklyMax = weeklyMax
                    )
                }
            }
        }
    }
}

@Composable
private fun HourlyCard(item: HourlyForecast?) {
    val weatherType = if (item != null) {
        WeatherType.fromWmoCode(item.weatherCode, item.isDay)
    } else {
        WeatherType.UNKNOWN
    }

    val timeText = if (item != null) {
        if (item.isNow) stringResource(R.string.now) else item.time
    } else {
        "--"
    }

    BaseCard(
        modifier = Modifier.width(65.dp)
    ) {
        Column(
            modifier = Modifier.padding(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            BaseText(
                text = timeText,
                textStyle = MaterialTheme.typography.bodyMedium
            )
            if (weatherType.lottieRes != null) {
                AnimLoad(resId = weatherType.lottieRes, modifier = Modifier.size(32.dp))
            } else {
                Box(modifier = Modifier.size(32.dp))
            }
            BaseText(
                text = item?.let { "${it.precipProbability}%" } ?: "--%",
                textStyle = MaterialTheme.typography.bodySmall,
            )
            BaseText(
                text = item?.let { "${it.temperature.roundToInt()}°" } ?: "--°",
                textStyle = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
private fun DailyRow(
    item: DailyForecast?,
    weeklyMin: Double,
    weeklyMax: Double
) {
    val weatherType = if (item != null) {
        WeatherType.fromWmoCode(item.weatherCode, isDay = true)
    } else {
        WeatherType.UNKNOWN
    }

    val dayText = if (item != null) {
        if (item.isToday) stringResource(R.string.today) else getDayName(item.dayOfWeek)
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
            textStyle = MaterialTheme.typography.titleMedium
        )
        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
            if (weatherType.lottieRes != null) {
                AnimLoad(resId = weatherType.lottieRes, modifier = Modifier.size(28.dp))
            } else {
                Box(modifier = Modifier.size(28.dp))
            }
        }
        Row(
            modifier = Modifier.weight(1.5f),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BaseText(
                text = item?.let { "${it.minTemp.roundToInt()}°" } ?: "--°",
                textStyle = MaterialTheme.typography.bodyLarge
            )
            TemperatureBar(
                minTemp = item?.minTemp ?: 0.0,
                maxTemp = item?.maxTemp ?: 0.0,
                weeklyMin = weeklyMin,
                weeklyMax = weeklyMax,
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

@Composable
private fun getDayName(dayOfWeek: Int): String {
    return stringResource(
        when (dayOfWeek) {
            1 -> R.string.dow_1
            2 -> R.string.dow_2
            3 -> R.string.dow_3
            4 -> R.string.dow_4
            5 -> R.string.dow_5
            6 -> R.string.dow_6
            7 -> R.string.dow_7
            else -> R.string.unknown
        }
    )
}