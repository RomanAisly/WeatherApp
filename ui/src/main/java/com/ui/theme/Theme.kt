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

@Immutable
data class BaseColors(
    val screenBack: Color,
    val text: Color,
    val textButton: Color,
    val bottomBarStart: Color,
    val bottomBarEnd: Color,
    val bottBarIconShadow: Color,
    val alertBackStart: Color,
    val alertBackEnd: Color,
    val bgCenter: Color,
    val bgHalo: Color,
    val bgEdge: Color,
    val iconTint: Color
) {
    val bottBarPortrait: Brush = Brush.verticalGradient(
        listOf(bottomBarStart, bottomBarEnd)
    )

    val bottBarLandscape: Brush = Brush.horizontalGradient(
        listOf(bottomBarEnd, bottomBarStart)
    )
    val alertBack: Brush = Brush.verticalGradient(
        listOf(alertBackStart, alertBackEnd)
    )
}

val lightColors = BaseColors(
    screenBack = mintCream,
    text = black,
    textButton = cornflowerBlue,
    bottomBarStart = azure,
    bottomBarEnd = skyBlue,
    bottBarIconShadow = iris,
    alertBackStart = lightBlue,
    alertBackEnd = skyBlue,
    bgCenter = azure,
    bgHalo = lightBlue,
    bgEdge = skyBlue,
    iconTint = cornflowerBlue
)

val darkColors = BaseColors(
    screenBack = twilight,
    text = white,
    textButton = deepSkyBlue,
    bottomBarStart = plum,
    bottomBarEnd = indigo,
    bottBarIconShadow = lightBlue,
    alertBackStart = plum,
    alertBackEnd = indigo,
    bgCenter = deepIndigo,
    bgHalo = plum,
    bgEdge = indigo,
    iconTint = white
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
        screenBack = animateColorAsState(targetColor.screenBack, animationSpec).value,
        text = animateColorAsState(targetColor.text, animationSpec).value,
        textButton = animateColorAsState(targetColor.textButton, animationSpec).value,
        bottomBarStart = animateColorAsState(targetColor.bottomBarStart, animationSpec).value,
        bottomBarEnd = animateColorAsState(targetColor.bottomBarEnd, animationSpec).value,
        bottBarIconShadow = animateColorAsState(targetColor.bottBarIconShadow, animationSpec).value,
        alertBackStart = animateColorAsState(targetColor.alertBackStart, animationSpec).value,
        alertBackEnd = animateColorAsState(targetColor.alertBackEnd, animationSpec).value,
        bgCenter = animateColorAsState(targetColor.bgCenter, animationSpec).value,
        bgHalo = animateColorAsState(targetColor.bgHalo, animationSpec).value,
        bgEdge = animateColorAsState(targetColor.bgEdge, animationSpec).value,
        iconTint = animateColorAsState(targetColor.iconTint, animationSpec).value
    )
}