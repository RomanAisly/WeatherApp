package com.ui.components

import android.content.res.Configuration
import android.graphics.BlurMaskFilter
import android.graphics.Paint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ui.theme.BaseTheme
import com.ui.theme.WeatherTheme
import com.ui.theme.gray
import com.ui.theme.limeGreen
import com.ui.theme.magenta
import com.ui.theme.red
import com.ui.theme.transparent
import com.ui.theme.vividOrange
import com.ui.theme.yellow
import com.weatherapp.ui.R

enum class LayoutMode {
    PORTRAIT,
    LANDSCAPE_PHONE,
    FOLD_TABLET
}

enum class WeatherType(val lottieRes: Int?, val title: Int, val staticIconRes: Int? = null) {
    UNKNOWN(null, R.string.unknown),

    CLEAR_DAY(R.raw.clear_day, R.string.clear, R.drawable.clear_day),
    CLEAR_NIGHT(R.raw.clear_night, R.string.clear, R.drawable.clear_night),

    MAINLY_CLEAR_DAY(R.raw.mainly_clear_day, R.string.fair, R.drawable.mainly_clear_day),
    MAINLY_CLEAR_NIGHT(R.raw.mainly_clear_night, R.string.fair, R.drawable.mainly_clear_night),

    PARTLY_CLOUDY_DAY(R.raw.partly_cloudy_day, R.string.cloudy, R.drawable.partly_cloudy_day),
    PARTLY_CLOUDY_NIGHT(R.raw.partly_cloudy_night, R.string.cloudy, R.drawable.partly_cloudy_night),
    OVERCAST(R.raw.overcast, R.string.overcast, R.drawable.overcast),
    FOG(R.raw.fog, R.string.fog, R.drawable.fog);

    companion object {
        fun fromWmoCode(code: Int, isDay: Boolean): WeatherType {
            return when (code) {
                0 -> if (isDay) CLEAR_DAY else CLEAR_NIGHT
                1 -> if (isDay) MAINLY_CLEAR_DAY else MAINLY_CLEAR_NIGHT
                2 -> if (isDay) PARTLY_CLOUDY_DAY else PARTLY_CLOUDY_NIGHT
                3 -> OVERCAST
                45, 48 -> FOG
                in 51..99 -> OVERCAST
                else -> UNKNOWN
            }
        }
    }
}

enum class WindStatus(val lottieRes: Int?, val title: Int) {
    UNKNOWN(null, R.string.unknown),
    CALM(R.raw.calm_wind, R.string.calm),
    LIGHT(R.raw.light_wind, R.string.light),
    GENTLE(R.raw.gentle_wind, R.string.gentle),
    MODERATE(R.raw.moderate_wind, R.string.moderate),
    STRONG(R.raw.tornado, R.string.strong);

    companion object {
        fun fromSpeed(speedKmH: Double): WindStatus {
            return when {
                speedKmH < 5.0 -> CALM
                speedKmH < 10.0 -> LIGHT
                speedKmH < 15.0 -> GENTLE
                speedKmH < 25.0 -> MODERATE
                else -> STRONG
            }
        }
    }
}

enum class PrecipitationType(
    val lottieRes: Int?,
    val staticIconRes: Int,
    val title: Int
) {
    NONE(null, R.drawable.dry, R.string.dry),
    DRIZZLE(R.raw.drizzle, R.drawable.drizzle, R.string.drizzle),
    RAIN(R.raw.rain, R.drawable.rain, R.string.rain),
    SNOW(R.raw.snow, R.drawable.snow, R.string.snow),
    FREEZING(R.raw.freezing_rain, R.drawable.freezing_rain, R.string.freezing),
    STORM(R.raw.thunderstorm, R.drawable.storm, R.string.storm);

    companion object {
        fun fromWmoCode(code: Int): PrecipitationType {
            return when (code) {
                51, 53, 55 -> DRIZZLE
                56, 57, 66, 67 -> FREEZING
                61, 63, 65, 80, 81, 82 -> RAIN
                71, 73, 75, 77, 85, 86 -> SNOW
                95, 96, 99 -> STORM
                else -> NONE
            }
        }
    }
}

enum class UvStatus(
    val title: Int,
    val lottieRes: Int? = R.raw.pulse,
    val lottieColor: Color? = null
) {
    UNKNOWN(R.string.unknown, null),
    LOW(R.string.uv_low, lottieColor = limeGreen),
    MODERATE(R.string.moderate, lottieColor = yellow),
    HIGH(R.string.uv_high, lottieColor = vividOrange),
    VERY_HIGH(R.string.uv_very_high, lottieColor = red),
    EXTREME(R.string.uv_extreme, lottieColor = magenta);

    companion object {
        fun fromIndex(uvIndex: Double): UvStatus {
            return when {
                uvIndex < 3.0 -> LOW
                uvIndex < 6.0 -> MODERATE
                uvIndex < 8.0 -> HIGH
                uvIndex < 11.0 -> VERY_HIGH
                else -> EXTREME
            }
        }
    }
}

@Composable
fun UvStatusIndicator(
    currentStatus: UvStatus,
    modifier: Modifier = Modifier
) {
    val activeStatuses = remember { UvStatus.entries.filter { it.lottieColor != null } }

    Spacer(
        modifier = modifier
            .fillMaxWidth()
            .height(16.dp)
            .drawWithCache {
                val circleRadiusPx = 4.dp.toPx()
                val glowRadiusPx = 8.dp.toPx()
                val spacingPx = 4.dp.toPx()
                val totalSpacing = spacingPx * (activeStatuses.size - 1)
                val segmentWidth = (size.width - totalSpacing) / activeStatuses.size
                val lineThicknessPx = 4.dp.toPx()
                val cornerRadius = CornerRadius(lineThicknessPx / 2, lineThicknessPx / 2)

                val glowPaint = Paint().apply {
                    this.color =
                        (currentStatus.lottieColor ?: gray).toArgb()
                    this.maskFilter = BlurMaskFilter(glowRadiusPx, BlurMaskFilter.Blur.NORMAL)
                }

                onDrawBehind {
                    var startX = 0f
                    activeStatuses.forEach { status ->
                        val color = status.lottieColor ?: gray
                        val isSelected = status == currentStatus
                        val alpha = if (isSelected) 1f else 0.4f

                        drawRoundRect(
                            color = color,
                            topLeft = Offset(
                                x = startX,
                                y = size.height / 2f - lineThicknessPx / 2f
                            ),
                            size = Size(width = segmentWidth, height = lineThicknessPx),
                            cornerRadius = cornerRadius,
                            alpha = alpha
                        )

                        if (isSelected) {
                            val centerX = startX + segmentWidth / 2f
                            val centerY = size.height / 2f

                            drawIntoCanvas { canvas ->
                                canvas.nativeCanvas.drawCircle(
                                    centerX,
                                    centerY,
                                    circleRadiusPx + 4.dp.toPx(),
                                    glowPaint
                                )
                            }
                            drawCircle(
                                color = color.lighten(),
                                radius = circleRadiusPx,
                                center = Offset(centerX, centerY)
                            )
                        }
                        startX += segmentWidth + spacingPx
                    }
                }
            }
    )
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

fun Modifier.neonGlow(
    color: Color,
    blurRadius: Dp = 2.dp,
    offsetY: Dp = 2.dp,
    shape: Shape
): Modifier = this.drawWithCache {
    if (blurRadius <= 0.dp || color == transparent) return@drawWithCache onDrawBehind { }
    val glowRadiusPx = blurRadius.toPx()
    val offsetYPx = offsetY.toPx()
    val outline = shape.createOutline(size, layoutDirection, this)

    val clipPath = Path().apply {
        when (outline) {
            is Outline.Rounded -> addRoundRect(outline.roundRect)
            is Outline.Rectangle -> addRect(outline.rect)
            is Outline.Generic -> addPath(outline.path)
        }
    }

    val paint = Paint().apply {
        this.color = color.toArgb()
        this.maskFilter = BlurMaskFilter(glowRadiusPx, BlurMaskFilter.Blur.NORMAL)
    }

    onDrawBehind {
        clipPath(clipPath, clipOp = ClipOp.Difference) {
            drawIntoCanvas { canvas ->
                when (outline) {
                    is Outline.Rounded -> {
                        val roundRect = outline.roundRect
                        val cornerRadius = roundRect.topLeftCornerRadius.x
                        canvas.nativeCanvas.drawRoundRect(
                            roundRect.left,
                            roundRect.top + offsetYPx,
                            roundRect.right,
                            roundRect.bottom + offsetYPx,
                            cornerRadius, cornerRadius,
                            paint
                        )
                    }

                    is Outline.Rectangle -> {
                        val rect = outline.rect
                        canvas.nativeCanvas.drawRect(
                            rect.left,
                            rect.top + offsetYPx,
                            rect.right,
                            rect.bottom + offsetYPx,
                            paint
                        )
                    }

                    is Outline.Generic -> {
                        canvas.save()
                        canvas.translate(0f, offsetYPx)
                        canvas.nativeCanvas.drawPath(
                            outline.path.asAndroidPath(),
                            paint
                        )
                        canvas.restore()
                    }
                }
            }
        }
    }
}

@Composable
fun TemperatureBar(
    minTemp: Double?,
    maxTemp: Double?,
    modifier: Modifier = Modifier
) {
    Spacer(
        modifier = modifier
            .height(6.dp)
            .drawWithCache {
                val cornerRadiusPx = CornerRadius(4.dp.toPx())

                val gradientBrush = if (minTemp != null && maxTemp != null) {
                    val colors = mutableListOf<Color>()
                    val steps = 5
                    for (i in 0..steps) {
                        val temp = minTemp + (maxTemp - minTemp) * (i / steps.toFloat())
                        colors.add(temp.toTempColor())
                    }
                    Brush.horizontalGradient(colors = colors)
                } else null

                onDrawBehind {
                    if (gradientBrush == null) {
                        drawRoundRect(
                            color = gray.copy(alpha = 0.3f),
                            size = size,
                            cornerRadius = cornerRadiusPx
                        )
                    } else {
                        drawRoundRect(
                            brush = gradientBrush,
                            topLeft = Offset(0f, 0f),
                            size = size,
                            cornerRadius = cornerRadiusPx
                        )
                    }
                }
            }
    )
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .radialScreenBackground(
                    centerColor = BaseTheme.colors.bgCenter,
                    haloColor = BaseTheme.colors.bgHalo,
                    edgeColor = BaseTheme.colors.bgEdge,
                ), contentAlignment = Alignment.BottomStart
        ) {
            ScreenLoader()
        }

    }
}