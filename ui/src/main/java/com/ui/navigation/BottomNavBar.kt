package com.ui.navigation

import android.graphics.BlurMaskFilter
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativePaint
import androidx.compose.ui.unit.dp
import com.ui.components.BaseIcon
import com.ui.components.LayoutMode
import com.ui.theme.BaseTheme
import com.ui.theme.transparent
import com.ui.theme.white
import com.weatherapp.ui.R

data class BottomTabItem(
    val route: Routes,
    val icon: Int
)

@Composable
fun BottomNavBar(
    currentTab: Any,
    layoutMode: LayoutMode,
    modifier: Modifier = Modifier,
    onTabSelected: (Routes) -> Unit
) {
    val bottomScreens = remember {
        listOf(
            BottomTabItem(route = Routes.Home, icon = R.drawable.home),
            BottomTabItem(route = Routes.Globe, icon = R.drawable.globe),
            BottomTabItem(route = Routes.Settings, icon = R.drawable.settings)
        )
    }

    val selectedIndex =
        bottomScreens.indexOfFirst { currentTab::class == it.route::class }.coerceAtLeast(0)

    val animatedIndex by animateFloatAsState(
        targetValue = selectedIndex.toFloat(),
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    val landGrad = BaseTheme.colors.bottBarLandscape
    val portGrad = BaseTheme.colors.bottBarPortrait

    if (layoutMode != LayoutMode.PORTRAIT) {
        Box(
            modifier = modifier
                .drawBehind {
                    val w = size.width
                    val h = size.height

                    val tabHeight = h / bottomScreens.size
                    val midY = (tabHeight * animatedIndex) + (tabHeight / 2f)

                    val dipH = tabHeight * 0.8f
                    val dipDepth = 30.dp.toPx()
                    val topY = midY - dipH / 2f
                    val bottomY = midY + dipH / 2f

                    val path = Path().apply {
                        moveTo(0f, 0f)
                        lineTo(w, 0f)
                        lineTo(w, topY)

                        cubicTo(
                            w, topY + dipH / 5f,
                            w - dipDepth, midY - dipH / 4f,
                            w - dipDepth, midY
                        )
                        cubicTo(
                            w - dipDepth, midY + dipH / 4f,
                            w, bottomY - dipH / 5f,
                            w, bottomY
                        )
                        lineTo(w, h)
                        lineTo(0f, h)
                        close()
                    }
                    drawPath(path = path, brush = landGrad)
                },
            contentAlignment = Alignment.CenterEnd
        ) {
            Column(
                modifier = Modifier
                    .width(72.dp)
                    .fillMaxHeight()
            ) {
                bottomScreens.forEach { item ->
                    TabItem(
                        item = item,
                        isSelected = currentTab::class == item.route::class,
                        isLandscape = true,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        onClick = { onTabSelected(item.route) }
                    )
                }
            }
        }
    } else {
        Box(
            modifier = Modifier
                .drawBehind {
                    val w = size.width
                    val h = size.height

                    val startY = 0f

                    val tabWidth = w / bottomScreens.size
                    val midX = (tabWidth * animatedIndex) + (tabWidth / 2f)

                    val dipW = tabWidth * 0.85f
                    val dipDepth = 30.dp.toPx()
                    val leftX = midX - dipW / 2f
                    val rightX = midX + dipW / 2f

                    val path = Path().apply {
                        moveTo(0f, startY)
                        lineTo(leftX, startY)

                        cubicTo(
                            leftX + dipW / 5f, startY,
                            midX - dipW / 4f, startY + dipDepth,
                            midX, startY + dipDepth
                        )
                        cubicTo(
                            midX + dipW / 4f, startY + dipDepth,
                            rightX - dipW / 5f, startY,
                            rightX, startY
                        )
                        lineTo(w, startY)
                        lineTo(w, h)
                        lineTo(0f, h)
                        close()
                    }
                    drawPath(path = path, brush = portGrad)
                }
                .navigationBarsPadding(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
            ) {
                bottomScreens.forEach { item ->
                    TabItem(
                        item = item,
                        isSelected = currentTab::class == item.route::class,
                        isLandscape = false,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        onClick = { onTabSelected(item.route) }
                    )
                }
            }
        }
    }
}

@Composable
private fun TabItem(
    item: BottomTabItem,
    isSelected: Boolean,
    isLandscape: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val offsetAnim by animateDpAsState(
        targetValue = if (isSelected) {
            if (isLandscape) 32.dp else (-32).dp
        } else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )
    val iconOpacity by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0.7f
    )
    val scaleAnim by animateFloatAsState(
        targetValue = if (isSelected) 1.4f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) white.copy(alpha = 0.2f) else transparent,
        animationSpec = tween(durationMillis = 500)
    )
    val iconShadow = BaseTheme.colors.bottBarIconShadow

    Box(
        modifier = modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onClick
        ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .graphicsLayer {
                    val offsetPx = offsetAnim.toPx()
                    if (isLandscape) {
                        translationX = offsetPx
                    } else {
                        translationY = offsetPx
                    }
                }
                .size(56.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .border(
                        width = 0.5.dp,
                        color = borderColor,
                        shape = CircleShape
                    )
                    .graphicsLayer {
                        scaleX = scaleAnim
                        scaleY = scaleAnim
                    }
                    .drawBehind {
                        drawIntoCanvas { canvas ->
                            val paint = Paint().apply {
                                color = iconShadow.copy(alpha = 0.6f)
                                nativePaint.maskFilter = BlurMaskFilter(
                                    16.dp.toPx(),
                                    BlurMaskFilter.Blur.NORMAL
                                )
                            }
                            canvas.drawCircle(
                                center = Offset(
                                    x = size.width / 2f,
                                    y = size.width / 2f + 4.dp.toPx()
                                ),
                                radius = 16.dp.toPx(),
                                paint = paint
                            )
                        }
                    }
            )
            BaseIcon(
                iconId = item.icon,
                iconTint = white,
                modifier = Modifier
                    .size(28.dp)
                    .graphicsLayer {
                        scaleX = scaleAnim
                        scaleY = scaleAnim
                        alpha = iconOpacity
                    }
            )
        }
    }
}