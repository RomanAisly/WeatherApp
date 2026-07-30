package com.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.weatherapp.ui.R

@Composable
fun AnimLoad() {
    Box(
        modifier = Modifier
            .padding(5.dp)
            .size(size = 100.dp), contentAlignment = Alignment.TopEnd,
        propagateMinConstraints = true
    ) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animation_lks55gli))
        val progress by animateLottieCompositionAsState(
            composition,
            iterations = LottieConstants.IterateForever,
            isPlaying = true
        )
        LottieAnimation(composition = composition, progress = { progress })
    }
}