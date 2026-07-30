package com.ui.components

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.ui.theme.unspecified

@Composable
fun BaseIcon(
    iconId: Int,
    modifier: Modifier = Modifier,
    iconTint: Color = unspecified
) {
    Icon(
        modifier = modifier,
        painter = painterResource(iconId),
        contentDescription = null,
        tint = iconTint
    )
}