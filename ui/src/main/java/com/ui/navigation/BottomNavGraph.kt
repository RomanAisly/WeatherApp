package com.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.ui.components.LayoutMode
import com.ui.screens.globe.GlobeScreen
import com.ui.screens.home.HomeScreen
import com.ui.screens.settings.SettingsScreen
import com.ui.theme.BaseTheme

@Composable
fun BottomNavGraph() {
    val backStack = rememberNavBackStack(Routes.Home)
    val currentTab = backStack.last()

    BackHandler(enabled = currentTab != Routes.Home) {
        backStack.clear()
        backStack.add(Routes.Home)
    }

    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val isWideScreen = windowSizeClass.isWidthAtLeastBreakpoint(600)
    val isCompactHeight = !windowSizeClass.isHeightAtLeastBreakpoint(480)
    val layoutDirection = LocalLayoutDirection.current

    val layoutMode = when {
        isCompactHeight -> LayoutMode.LANDSCAPE_PHONE
        isWideScreen -> LayoutMode.FOLD_TABLET
        else -> LayoutMode.PORTRAIT
    }

    val showSideNav = layoutMode == LayoutMode.LANDSCAPE_PHONE

    val slideDuration = 550
    val fadeDuration = 450

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            containerColor = BaseTheme.colors.scaffoldBack,
            bottomBar = {
                if (!showSideNav) {
                    BottomNavBar(
                        currentTab = currentTab,
                        layoutMode = layoutMode,
                        onTabSelected = { tabRoute ->
                            if (currentTab != tabRoute) {
                                backStack[0] = tabRoute
                            }
                        })
                }
            }
        ) { scaffoldPadding ->
            val dynamicPadding = if (showSideNav) {
                PaddingValues(
                    start = scaffoldPadding.calculateStartPadding(layoutDirection) + 60.dp,
                    top = scaffoldPadding.calculateTopPadding(),
                    end = scaffoldPadding.calculateEndPadding(layoutDirection),
                    bottom = scaffoldPadding.calculateBottomPadding()
                )
            } else {
                scaffoldPadding
            }

            NavDisplay(
                backStack = backStack,
                modifier = Modifier
                    .fillMaxSize(),
                transitionSpec = {
                    (fadeIn(animationSpec = tween(fadeDuration)) + slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(slideDuration)
                    )) togetherWith (fadeOut(animationSpec = tween(fadeDuration)) + slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(slideDuration)
                    ))
                },
                popTransitionSpec = {
                    (fadeIn(animationSpec = tween(fadeDuration)) + slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(slideDuration)
                    )) togetherWith (fadeOut(animationSpec = tween(fadeDuration)) + slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(slideDuration)
                    ))
                },
                entryProvider = entryProvider {

                    entry<Routes.Home> {
                        HomeScreen(paddingValues = dynamicPadding, layoutMode = layoutMode)
                    }
                    entry<Routes.Globe> {
                        GlobeScreen(paddingValues = dynamicPadding)
                    }
                    entry<Routes.Settings> {
                        SettingsScreen(paddingValues = dynamicPadding)
                    }
                })
            if (showSideNav) {
                BottomNavBar(
                    currentTab = currentTab,
                    layoutMode = layoutMode,
                    modifier = Modifier.align(Alignment.CenterStart),
                    onTabSelected = { tabRoute ->
                        if (currentTab != tabRoute) backStack[0] = tabRoute
                    }
                )
            }
        }
    }
}