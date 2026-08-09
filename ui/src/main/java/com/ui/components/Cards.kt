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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ui.theme.BaseTheme
import com.ui.theme.lightBlue
import com.ui.theme.silver
import com.ui.theme.transparent
import com.weatherapp.ui.R

@Composable
fun BaseCard(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.large,
    containerColor: Color = transparent,
    border: BorderStroke? = null,
    backGrad: Brush? = BaseTheme.colors.cardBack,
    glowColor: Color = BaseTheme.colors.cardGlow,
    glowRadius: Dp = 4.dp,
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
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = border,
        content = content
    )
}

@Composable
fun TimeFooter(
    timeDuration: String,
    textStyle: TextStyle = MaterialTheme.typography.titleMedium
) {
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
            textStyle = textStyle
        )
    }
}

@Composable
fun WidgetCard(
    title: String,
    iconRes: Int,
    valueText: String,
    lottieRes: Int?,
    modifier: Modifier = Modifier,
    lottieTint: Color? = null,
    iconTint: Color = BaseTheme.colors.widgetIcon,
    bottomContent: @Composable () -> Unit
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
                BaseIcon(iconRes, iconTint = iconTint)
                BaseText(
                    text = title,
                    textStyle = MaterialTheme.typography.titleSmall,
                    maxLines = 1
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (lottieRes != null) {
                    AnimLoad(
                        resId = lottieRes,
                        tintColor = lottieTint,
                        modifier = Modifier.size(38.dp),
                    )
                } else {
                    Box(modifier = Modifier.size(38.dp))
                }
                BaseText(
                    text = valueText,
                    textStyle = MaterialTheme.typography.titleMedium
                )
            }
            bottomContent()
        }
    }
}

@Composable
fun WeatherCard(
    weatherType: WeatherType,
    cloudCover: String,
    timeDuration: String,
    modifier: Modifier = Modifier
) {
    WidgetCard(
        title = stringResource(weatherType.title),
        iconRes = R.drawable.cloud,
        valueText = cloudCover,
        lottieRes = weatherType.lottieRes,
        lottieTint = if (weatherType == WeatherType.OVERCAST) silver else null,
        modifier = modifier
    ) {
        TimeFooter(timeDuration)
    }
}

@Composable
fun WindCard(
    windStrength: String,
    windStatus: WindStatus,
    timeDuration: String,
    modifier: Modifier = Modifier
) {
    val lottieTint = when (windStatus) {
        WindStatus.GENTLE -> lightBlue
        WindStatus.LIGHT -> BaseTheme.colors.text
        else -> null
    }

    WidgetCard(
        title = stringResource(windStatus.title),
        iconRes = R.drawable.wind,
        valueText = stringResource(R.string.kmh_abbr, windStrength),
        lottieRes = windStatus.lottieRes,
        lottieTint = lottieTint,
        modifier = modifier
    ) {
        TimeFooter(timeDuration)
    }
}

@Composable
fun PrecipitationCard(
    amount: String,
    type: PrecipitationType,
    timeDuration: String,
    modifier: Modifier = Modifier
) {
    WidgetCard(
        title = stringResource(type.title),
        iconRes = type.staticIconRes,
        valueText = amount,
        lottieRes = type.lottieRes,
        lottieTint = if (type == PrecipitationType.RAIN) lightBlue else null,
        modifier = modifier
    ) {
        TimeFooter(timeDuration, textStyle = MaterialTheme.typography.titleSmall)
    }
}

@Composable
fun UvCard(
    uvIndex: String,
    uvStatus: UvStatus,
    modifier: Modifier = Modifier
) {
    WidgetCard(
        title = stringResource(uvStatus.title),
        iconRes = R.drawable.uv,
        valueText = uvIndex,
        lottieRes = uvStatus.lottieRes,
        lottieTint = uvStatus.lottieColor,
        modifier = modifier
    ) {
        UvStatusIndicator(
            currentStatus = uvStatus
        )
    }
}