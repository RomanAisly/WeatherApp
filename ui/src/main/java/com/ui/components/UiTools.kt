package com.ui.components

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigationevent.compose.LocalNavigationEventDispatcherOwner
import androidx.navigationevent.compose.rememberNavigationEventDispatcherOwner
import com.ui.theme.WeatherTheme
import com.weatherapp.ui.R
import org.koin.compose.KoinContext
import org.koin.dsl.koinApplication

enum class LayoutMode {
    PORTRAIT,
    LANDSCAPE_PHONE,
    FOLD_TABLET
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
    startOffset: Dp = 0.dp
): Modifier = this.drawWithCache {

    val topPx = topOffset.toPx()
    val startPx = startOffset.toPx()

    val workingHeight = size.height - topPx
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
    val koin = remember {
        koinApplication {
            modules(previewModule)
        }.koin
    }
    val navigationEventDispatcherOwner = rememberNavigationEventDispatcherOwner(parent = null)
    KoinContext(context = koin) {
        CompositionLocalProvider(LocalNavigationEventDispatcherOwner provides navigationEventDispatcherOwner) {
            WeatherTheme(onThemeChange = {}) {

            }
        }
    }
}