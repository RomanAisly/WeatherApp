package com.ui.components

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ui.theme.WeatherTheme
import com.weatherapp.ui.R

enum class LayoutMode {
    PORTRAIT,
    LANDSCAPE_PHONE,
    FOLD_TABLET
}

enum class WeatherType(val lottieRes: Int, val title: Int) {
    CLEAR_DAY(R.raw.clear_day, R.string.clear),
    CLEAR_NIGHT(R.raw.clear_night, R.string.clear),

    MAINLY_CLEAR_DAY(R.raw.mainly_clear_day, R.string.fair),
    MAINLY_CLEAR_NIGHT(R.raw.mainly_clear_night, R.string.fair),

    PARTLY_CLOUDY_DAY(R.raw.partly_cloudy_day, R.string.cloudy),
    PARTLY_CLOUDY_NIGHT(R.raw.partly_cloudy_night, R.string.cloudy),
    OVERCAST(R.raw.overcast, R.string.overcast);

    companion object {
        fun fromWmoCode(code: Int, isDay: Boolean): WeatherType {
            return when (code) {
                0 -> if (isDay) CLEAR_DAY else CLEAR_NIGHT
                1 -> if (isDay) MAINLY_CLEAR_DAY else MAINLY_CLEAR_NIGHT
                2 -> if (isDay) PARTLY_CLOUDY_DAY else PARTLY_CLOUDY_NIGHT
                3 -> OVERCAST
                else -> OVERCAST
            }
        }
    }
}

enum class WindStatus(val lottieRes: Int) {
    EASY(R.raw.light_wind),
    MIDDLE(R.raw.middle_wind),
    STRONG(R.raw.tornado);

    companion object {
        fun fromSpeed(speedKmH: Double): WindStatus {
            return when {
                speedKmH < 15.0 -> EASY
                speedKmH < 35.0 -> MIDDLE
                else -> STRONG
            }
        }
    }
}

fun Modifier.radialScreenBackground(
    centerColor: Color,
    haloColor: Color,
    edgeColor: Color,
    topOffset: Dp = 0.dp,
    bottomOffset: Dp = 0.dp,
    startOffset: Dp = 0.dp
): Modifier = this.drawWithCache {

    val topPx = topOffset.toPx()
    val bottomPx = bottomOffset.toPx()
    val startPx = startOffset.toPx()

    val workingHeight = size.height - topPx - bottomPx
    val workingWidth = size.width - startPx

    val centerX = startPx + (workingWidth / 2f)
    val centerY = topPx + (workingHeight * 0.3f)
    val spotRadius = size.width * 0.6f

    val shiftedBrush = Brush.radialGradient(
        0.1f to centerColor,
        0.4f to haloColor,
        1.0f to edgeColor,
        center = Offset(centerX, centerY),
        radius = spotRadius
    )
    onDrawBehind {
        drawRect(brush = shiftedBrush)
    }
}

@Composable
@Preview(
    name = "Light Mode",
    showBackground = true,
    showSystemUi = true
)
@Preview(
    name = "Dark Mode",
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
internal fun UiToolsPreview() {
    WeatherTheme(onThemeChange = {}) {

    }
}