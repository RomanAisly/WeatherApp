package com.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ui.theme.BaseTheme
import com.ui.theme.deepIndigo
import com.ui.theme.lightBlue
import com.ui.theme.skyBlue
import com.ui.theme.transparent
import com.weatherapp.ui.R

@Composable
fun BaseCard(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.large,
    containerColor: Color = deepIndigo,
    elevation: Dp = 4.dp,
    shadowColor: Color = BaseTheme.colors.text,
    border: BorderStroke? = null,
    backGrad: Brush? = null,
    content: @Composable (ColumnScope.() -> Unit)
) {
    Card(
        modifier = modifier
            .then(
                if (elevation > 0.dp && shadowColor != transparent) {
                    Modifier.shadow(
                        elevation = elevation,
                        shape = shape,
                        ambientColor = shadowColor,
                        spotColor = shadowColor
                    )
                } else Modifier
            )
            .then(
                if (backGrad != null) {
                    Modifier.background(brush = backGrad, shape = shape)
                } else Modifier
            ),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = if (backGrad != null) transparent else containerColor),
        border = border,
        content = content
    )
}

@Composable
fun WeatherCard(
    weatherType: WeatherType,
    cloudCover: String,
    timeDuration: String,
    modifier: Modifier = Modifier
) {
    BaseCard(modifier = modifier) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BaseIcon(R.drawable.cloud, iconTint = skyBlue)
                BaseText(
                    text = stringResource(weatherType.title),
                    textStyle = MaterialTheme.typography.titleSmall
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (weatherType.lottieRes != null) {
                    AnimLoad(
                        resId = weatherType.lottieRes,
                        modifier = Modifier.size(38.dp),
                    )
                } else {
                    Box(modifier = Modifier.size(38.dp))
                }
                BaseText(
                    text = cloudCover,
                    textStyle = MaterialTheme.typography.titleMedium
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BaseText(
                    text = stringResource(R.string.time)
                )
                BaseText(
                    text = stringResource(R.string.hour_format, timeDuration),
                    textStyle = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
fun WindCard(
    windStrength: String,
    windStatus: WindStatus,
    timeDuration: String,
    modifier: Modifier = Modifier
) {
    BaseCard(modifier = modifier) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BaseIcon(R.drawable.wind, iconTint = skyBlue)
                BaseText(
                    stringResource(windStatus.title),
                    textStyle = MaterialTheme.typography.titleSmall
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (windStatus.lottieRes != null) {
                    AnimLoad(
                        resId = windStatus.lottieRes,
                        tintColor = if (windStatus == WindStatus.GENTLE) lightBlue else null,
                        modifier = Modifier.size(38.dp),
                    )
                } else {
                    Box(modifier = Modifier.size(38.dp))
                }
                BaseText(
                    stringResource(R.string.kmh_abbr, windStrength),
                    textStyle = MaterialTheme.typography.titleMedium
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BaseText(
                    text = stringResource(R.string.time)
                )
                BaseText(
                    text = stringResource(R.string.hour_format, timeDuration),
                    textStyle = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
fun PrecipitationCard(
    amount: String,
    type: PrecipitationType,
    timeDuration: String,
    modifier: Modifier = Modifier
) {
    BaseCard(modifier = modifier) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BaseIcon(iconId = type.staticIconRes, iconTint = skyBlue)
                BaseText(
                    text = stringResource(type.title),
                    textStyle = MaterialTheme.typography.titleSmall
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (type.lottieRes != null) {
                    AnimLoad(
                        resId = type.lottieRes,
                        tintColor = if (type == PrecipitationType.RAIN) lightBlue else null,
                        modifier = Modifier.size(38.dp),
                    )
                } else {
                    Box(modifier = Modifier.size(38.dp))
                }
                BaseText(
                    text = amount,
                    textStyle = MaterialTheme.typography.titleMedium
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BaseText(
                    text = stringResource(R.string.time)
                )
                BaseText(
                    text = stringResource(R.string.hour_format, timeDuration),
                    textStyle = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}