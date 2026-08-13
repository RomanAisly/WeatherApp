package com.ui.theme

import android.app.Activity
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.domain.AppTheme
import com.ui.components.darken

@Immutable
data class BaseColors(
    val scaffoldBack: Color,
    val text: Color,
    val textButton: Color,
    val bottomBarStart: Color,
    val bottomBarEnd: Color,
    val bottBarIconShadow: Color,
    val bgCenter: Color,
    val bgHalo: Color,
    val bgEdge: Color,
    val iconTint: Color,
    val widgetIcon: Color,
    val cardStart: Color,
    val cardEnd: Color,
    val alertStart: Color,
    val alertEnd: Color,
    val cardGlow: Color,
    val rain: Color,
    val stroke: Color
) {
    val bottBarPortrait: Brush = Brush.verticalGradient(
        listOf(bottomBarStart, bottomBarEnd)
    )
    val bottBarLandscape: Brush = Brush.horizontalGradient(
        listOf(bottomBarEnd, bottomBarStart)
    )
    val alertBack: Brush = Brush.verticalGradient(
        listOf(alertStart, alertEnd)
    )
    val settScreenBack: Brush = Brush.horizontalGradient(
        listOf(cardEnd, bottomBarEnd)
    )
    val cardBack: Brush = Brush.linearGradient(
        listOf(cardStart, cardEnd)
    )
}

val lightColors = BaseColors(
    scaffoldBack = azure,
    text = deepDarkGray,
    textButton = royalBlue.darken(0.1f),
    bottomBarStart = skyBlue,
    bottomBarEnd = mintCream,
    bottBarIconShadow = iris.darken(),
    bgCenter = mintCream,
    bgHalo = skyBlue,
    bgEdge = azure,
    iconTint = twilight,
    widgetIcon = deepSkyBlue,
    cardStart = mintCream.copy(alpha = 0.5f),
    cardEnd = skyBlue.copy(alpha = 0.5f),
    alertStart = mintCream.copy(alpha = 0.5f),
    alertEnd = lightGray.copy(alpha = 0.5f),
    cardGlow = gray,
    rain = deepSkyBlue,
    stroke = cornflowerBlue
)

val darkColors = BaseColors(
    scaffoldBack = indigo,
    text = white,
    textButton = deepSkyBlue,
    bottomBarStart = plum,
    bottomBarEnd = indigo,
    bottBarIconShadow = lightBlue,
    bgCenter = deepIndigo,
    bgHalo = plum,
    bgEdge = indigo,
    iconTint = white,
    widgetIcon = lightBlue,
    cardStart = lightStateGray.copy(alpha = 0.5f),
    cardEnd = deepDarkGray.copy(alpha = 0.5f),
    alertStart = indigo.copy(alpha = 0.5f),
    alertEnd = twilight.copy(alpha = 0.5f),
    cardGlow = persianGreen,
    rain = lightBlue,
    stroke = yellow
)

object BaseTheme {
    val colors: BaseColors
        @Composable
        get() = LocalBaseColors.current
}

val LocalBaseColors = compositionLocalOf<BaseColors> { error("No AppColors provided") }
val LocalSetTheme = staticCompositionLocalOf { AppTheme.SYSTEM }
val LocalThemeChangeHandler = staticCompositionLocalOf<(AppTheme) -> Unit> { {} }

@Composable
fun WeatherTheme(
    setTheme: AppTheme = AppTheme.SYSTEM,
    onThemeChange: (AppTheme) -> Unit,
    content: @Composable () -> Unit
) {
    val isDark = when (setTheme) {
        AppTheme.LIGHT -> false
        AppTheme.DARK -> true
        AppTheme.SYSTEM -> isSystemInDarkTheme()
    }

    val colorScheme = if (isDark) darkColors else lightColors
    val animatedColorScheme = animateColorSchemeAsState(colorScheme)

    val view = LocalView.current
    if (!view.isInEditMode) {
        LaunchedEffect(isDark) {
            val window = (view.context as Activity).window
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = !isDark
            insetsController.isAppearanceLightNavigationBars = !isDark
        }
    }

    CompositionLocalProvider(
        LocalSetTheme provides setTheme,
        LocalThemeChangeHandler provides onThemeChange,
        LocalBaseColors provides animatedColorScheme
    ) {
        MaterialTheme(
            typography = Typography,
            content = content
        )
    }
}

@Composable
private fun animateColorSchemeAsState(targetColor: BaseColors): BaseColors {
    val animationSpec = tween<Color>(durationMillis = 400)
    return BaseColors(
        scaffoldBack = animateColorAsState(targetColor.scaffoldBack, animationSpec).value,
        text = animateColorAsState(targetColor.text, animationSpec).value,
        textButton = animateColorAsState(targetColor.textButton, animationSpec).value,
        bottomBarStart = animateColorAsState(targetColor.bottomBarStart, animationSpec).value,
        bottomBarEnd = animateColorAsState(targetColor.bottomBarEnd, animationSpec).value,
        bottBarIconShadow = animateColorAsState(targetColor.bottBarIconShadow, animationSpec).value,
        bgCenter = animateColorAsState(targetColor.bgCenter, animationSpec).value,
        bgHalo = animateColorAsState(targetColor.bgHalo, animationSpec).value,
        bgEdge = animateColorAsState(targetColor.bgEdge, animationSpec).value,
        iconTint = animateColorAsState(targetColor.iconTint, animationSpec).value,
        widgetIcon = animateColorAsState(targetColor.widgetIcon, animationSpec).value,
        cardStart = animateColorAsState(targetColor.cardStart, animationSpec).value,
        cardEnd = animateColorAsState(targetColor.cardEnd, animationSpec).value,
        alertStart = animateColorAsState(targetColor.alertStart, animationSpec).value,
        alertEnd = animateColorAsState(targetColor.alertEnd, animationSpec).value,
        cardGlow = animateColorAsState(targetColor.cardGlow, animationSpec).value,
        rain = animateColorAsState(targetColor.rain, animationSpec).value,
        stroke = animateColorAsState(targetColor.stroke, animationSpec).value
    )
}