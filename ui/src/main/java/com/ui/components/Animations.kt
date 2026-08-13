package com.ui.components

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.RenderMode
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.lottie.compose.rememberLottieDynamicProperties
import com.airbnb.lottie.compose.rememberLottieDynamicProperty
import com.ui.theme.BaseTheme
import com.ui.theme.black
import com.ui.theme.lightGray
import com.ui.theme.white
import com.weatherapp.ui.R

@Composable
fun AnimLoad(
    resId: Int,
    modifier: Modifier = Modifier,
    tintColor: Color? = null
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
        propagateMinConstraints = true
    ) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(resId))
        val dynamicProperties = if (tintColor != null) {
            val colorFilter = remember(tintColor) {
                PorterDuffColorFilter(tintColor.toArgb(), PorterDuff.Mode.SRC_ATOP)
            }
            rememberLottieDynamicProperties(
                rememberLottieDynamicProperty(
                    property = LottieProperty.COLOR_FILTER,
                    value = colorFilter,
                    keyPath = arrayOf("**")
                )
            )
        } else {
            null
        }

        LottieAnimation(
            composition = composition,
            dynamicProperties = dynamicProperties,
            contentScale = ContentScale.Crop,
            isPlaying = true,
            iterations = LottieConstants.IterateForever,
            renderMode = RenderMode.HARDWARE
        )
    }
}

@Composable
fun <T> FadeWrapper(
    targetState: T,
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.Center,
    content: @Composable (T) -> Unit
) {
    AnimatedContent(
        targetState = targetState,
        transitionSpec = {
            fadeIn(animationSpec = tween(700)) togetherWith fadeOut(animationSpec = tween(700))
        },
        modifier = modifier,
        contentAlignment = contentAlignment
    ) { state ->
        content(state)
    }
}

@Composable
fun ScreenLoader(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(black.copy(alpha = 0.6f))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {}
            )
            .zIndex(10f),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(26.dp)
        ) {
            CircularProgressIndicator(
                modifier = modifier.size(80.dp),
                color = BaseTheme.colors.stroke,
                strokeWidth = 8.dp,
                trackColor = lightGray
            )
            BaseText(
                text = stringResource(R.string.finding_your_location),
                textColor = white,
                textStyle = MaterialTheme.typography.titleMedium
            )
        }
    }
}