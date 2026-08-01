package com.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ui.theme.BaseTheme
import com.ui.theme.deepIndigo
import com.ui.theme.limeGreen
import com.ui.theme.skyBlue
import com.ui.theme.transparent
import com.weatherapp.ui.R

@Composable
fun BaseCard(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.large,
    containerColor: Color = deepIndigo,
    elevation: Dp = 2.dp,
    shadowColor: Color = BaseTheme.colors.text,
    border: BorderStroke? = null,
    backGrad: Brush? = null,
    content: @Composable (ColumnScope.() -> Unit)
) {
    Card(
        modifier = modifier
            .then(
                if (elevation > 0.dp && shadowColor != transparent) {
                    Modifier.shadow(
                        elevation = elevation,
                        shape = shape,
                        ambientColor = shadowColor,
                        spotColor = shadowColor
                    )
                } else Modifier
            )
            .then(
                if (backGrad != null) {
                    Modifier.background(brush = backGrad, shape = shape)
                } else Modifier
            ),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = if (backGrad != null) transparent else containerColor),
        border = border,
        content = content
    )
}

@Composable
fun WindCard(
    windStrength: String,
    windStatus: WindStatus,
    modifier: Modifier = Modifier
) {
    BaseCard(modifier = modifier) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BaseIcon(R.drawable.wind, iconTint = skyBlue)
                BaseText("WIND", textStyle = MaterialTheme.typography.bodyLarge)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AnimLoad(
                    windStatus.lottieRes,
                    modifier = Modifier.size(32.dp),
                    tintColor = limeGreen
                )
                BaseText(windStrength, textStyle = MaterialTheme.typography.titleLarge)
            }
        }

    }
}