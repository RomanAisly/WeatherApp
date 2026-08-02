package com.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ui.theme.BaseTheme

@Composable
fun BaseIcon(
    iconId: Int,
    modifier: Modifier = Modifier,
    iconTint: Color = BaseTheme.colors.iconTint
) {
    Icon(
        modifier = modifier,
        painter = painterResource(iconId),
        contentDescription = null,
        tint = iconTint
    )
}

@Composable
fun BaseIconButton(
    iconId: Int,
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    iconTint: Color = BaseTheme.colors.text,
    onClick: () -> Unit
) {
    IconButton(
        modifier = modifier.size(size),
        onClick = onClick
    ) {
        Icon(
            painter = painterResource(iconId),
            contentDescription = null,
            tint = iconTint
        )
    }
}