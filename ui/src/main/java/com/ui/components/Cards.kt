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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ui.theme.BaseTheme
import com.ui.theme.lightBlue
import com.ui.theme.transparent
import com.weatherapp.ui.R

@Composable
fun BaseCard(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.large,
    containerColor: Color = transparent,
    elevation: Dp = 0.dp,
    border: BorderStroke? = null,
    backGrad: Brush? = BaseTheme.colors.cardBack,
    glowColor: Color = BaseTheme.colors.cardGlow,
    glowRadius: Dp = 6.dp,
    content: @Composable (ColumnScope.() -> Unit)
) {
    Card(
        modifier = modifier
            .neonGlow(
                color = glowColor,
                blurRadius = glowRadius,
                shape = shape
            )
            .then(
                if (backGrad != null) {
                    Modifier.background(brush = backGrad, shape = shape)
                } else Modifier
            ),
        shape = shape,
        elevation = CardDefaults.cardElevation(elevation),
        colors = CardDefaults.cardColors(containerColor = containerColor),
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
    BaseCard(
        modifier = modifier,
        elevation = 0.dp
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BaseIcon(R.drawable.cloud, iconTint = BaseTheme.colors.widgetIcon)
                BaseText(
                    text = stringResource(weatherType.title),
                    textStyle = MaterialTheme.typography.titleSmall,
                    maxLines = 1
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
                    text = stringResource(R.string.time),
                    maxLines = 1
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
    BaseCard(
        modifier = modifier,
        elevation = 0.dp
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BaseIcon(R.drawable.wind, iconTint = BaseTheme.colors.widgetIcon)
                BaseText(
                    stringResource(windStatus.title),
                    textStyle = MaterialTheme.typography.titleSmall,
                    maxLines = 1
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
                        tintColor = if (windStatus == WindStatus.GENTLE) lightBlue else if (windStatus == WindStatus.LIGHT) {
                            BaseTheme.colors.text
                        } else null,
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
                    text = stringResource(R.string.time),
                    maxLines = 1
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
    BaseCard(
        modifier = modifier,
        elevation = 0.dp
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BaseIcon(iconId = type.staticIconRes, iconTint = BaseTheme.colors.widgetIcon)
                BaseText(
                    text = stringResource(type.title),
                    textStyle = MaterialTheme.typography.titleSmall,
                    maxLines = 1
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
                    text = stringResource(R.string.time),
                    maxLines = 1
                )
                BaseText(
                    text = stringResource(R.string.hour_format, timeDuration),
                    textStyle = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}

@Composable
fun UvCard(
    uvIndex: String,
    uvStatus: UvStatus,
    modifier: Modifier = Modifier
) {
    BaseCard(
        modifier = modifier,
        elevation = 0.dp
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BaseIcon(R.drawable.uv, iconTint = BaseTheme.colors.widgetIcon)
                BaseText(
                    text = stringResource(uvStatus.title),
                    textStyle = MaterialTheme.typography.titleSmall,
                    maxLines = 1
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (uvStatus.lottieRes != null) {
                    AnimLoad(
                        resId = uvStatus.lottieRes,
                        tintColor = uvStatus.lottieColor,
                        modifier = Modifier.size(40.dp),
                    )
                } else {
                    Box(modifier = Modifier.size(40.dp))
                }
                BaseText(
                    text = uvIndex,
                    textStyle = MaterialTheme.typography.titleMedium
                )
            }
            UvStatusIndicator(
                currentStatus = uvStatus,
                modifier = Modifier.align(Alignment.Start)
            )
        }
    }
}