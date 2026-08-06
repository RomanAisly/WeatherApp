package com.ui.components

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.lottie.compose.rememberLottieDynamicProperties
import com.airbnb.lottie.compose.rememberLottieDynamicProperty

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
        val progress by animateLottieCompositionAsState(
            composition,
            iterations = LottieConstants.IterateForever,
            isPlaying = true
        )
        val dynamicProperties = rememberLottieDynamicProperties(
            rememberLottieDynamicProperty(
                property = LottieProperty.COLOR_FILTER,
                value = tintColor?.let { color ->
                    PorterDuffColorFilter(
                        color.toArgb(),
                        PorterDuff.Mode.SRC_ATOP
                    )
                },
                keyPath = arrayOf("**")
            )
        )

        LottieAnimation(
            composition = composition,
            progress = { progress },
            dynamicProperties = dynamicProperties,
            contentScale = ContentScale.Crop
        )
    }
}