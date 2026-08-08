package com.ui.screens.globe

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.domain.models.DailyForecast
import com.domain.models.HourlyForecast
import com.ui.components.AnimLoad
import com.ui.components.BaseCard
import com.ui.components.BaseText
import com.ui.components.PrecipitationType
import com.ui.components.TemperatureBar
import com.ui.components.WeatherType
import com.ui.components.WindStatus
import com.ui.components.toDayNameRes
import com.ui.theme.BaseTheme
import com.ui.theme.lightBlue
import com.weatherapp.ui.R
import org.koin.androidx.compose.koinViewModel
import kotlin.math.roundToInt

@Composable
fun GlobeScreen(
    paddingValues: PaddingValues,
    viewModel: GlobeViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val displayHourly = state.hourlyForecasts.ifEmpty { List(24) { null } }
    val displayDaily = state.dailyForecasts.ifEmpty { List(10) { null } }


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
                .padding(top = paddingValues.calculateTopPadding() + 16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(displayHourly) { hourlyItem ->
                HourlyWindCard(item = hourlyItem)
            }
        }
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(displayHourly) { hourlyItem ->
                HourlyPrecipCard(item = hourlyItem)
            }
        }
        BaseCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    bottom = paddingValues.calculateBottomPadding() + 16.dp,
                    start = 16.dp,
                    end = 16.dp
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
                        item = dailyItem,
                    )
                }
            }
        }
    }
}

@Composable
private fun HourlyWindCard(item: HourlyForecast?) {
    val windStatus = if (item != null) WindStatus.fromSpeed(item.windSpeed) else WindStatus.UNKNOWN
    val timeText = item?.time ?: "--"

    BaseCard(modifier = Modifier.width(70.dp)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            BaseText(text = timeText)

            if (windStatus.lottieRes != null) {
                AnimLoad(
                    resId = windStatus.lottieRes, tintColor = when (windStatus) {
                        WindStatus.GENTLE -> lightBlue
                        WindStatus.LIGHT -> {
                            BaseTheme.colors.text
                        }

                        else -> null
                    }, modifier = Modifier.size(28.dp)
                )
            } else {
                Box(modifier = Modifier.size(28.dp))
            }
            BaseText(
                text = item?.let { "${it.windSpeed.roundToInt()}" } ?: "--"
            )
            BaseText(
                text = item?.let { "${it.temperature.roundToInt()}°" } ?: "--°",
                textStyle = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
private fun HourlyPrecipCard(item: HourlyForecast?) {
    val precipType =
        if (item != null) PrecipitationType.fromWmoCode(item.weatherCode) else PrecipitationType.NONE
    val timeText = item?.time ?: "--"

    BaseCard(modifier = Modifier.width(70.dp)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            BaseText(text = timeText)

            if (precipType.lottieRes != null) {
                AnimLoad(resId = precipType.lottieRes, modifier = Modifier.size(28.dp))
            } else {
                Box(modifier = Modifier.size(28.dp))
            }
            BaseText(
                text = item?.let { "${it.precipitation} mm" } ?: "-- mm",
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
private fun DailyCard(
    item: DailyForecast?,

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
            textStyle = MaterialTheme.typography.titleMedium,
            maxLines = 1
        )
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (weatherType.lottieRes != null) {
                AnimLoad(resId = weatherType.lottieRes, modifier = Modifier.size(28.dp))
            } else {
                Box(modifier = Modifier.size(28.dp))
            }

            if (precipType.lottieRes != null) {
                AnimLoad(
                    resId = precipType.lottieRes,
                    modifier = Modifier.size(28.dp),
                    tintColor = if (precipType == PrecipitationType.RAIN) lightBlue else null
                )
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