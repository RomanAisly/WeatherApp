package com.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.ui.components.LayoutMode
import com.ui.screens.GlobeScreen
import com.ui.screens.HomeScreen
import com.ui.screens.SettingsScreen
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

    val layoutMode = when {
        isCompactHeight -> LayoutMode.LANDSCAPE_PHONE
        isWideScreen -> LayoutMode.FOLD_TABLET
        else -> LayoutMode.PORTRAIT
    }

    val showSideNav = layoutMode != LayoutMode.PORTRAIT

    val slideDuration = 550
    val fadeDuration = 450

    Scaffold(
        containerColor = BaseTheme.colors.screenBack,
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
    ) { paddingValues ->
        Row(
            modifier = Modifier
                .fillMaxSize()
        ) {
            if (showSideNav) {
                BottomNavBar(
                    currentTab = currentTab, layoutMode = layoutMode,
                    onTabSelected = { tabRoute ->
                        if (currentTab != tabRoute) {
                            backStack[0] = tabRoute
                        }
                    })
            }
            NavDisplay(
                backStack = backStack,
                modifier = Modifier
                    .weight(1f)
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
                        HomeScreen(paddingValues = paddingValues)
                    }
                    entry<Routes.Globe> {
                        GlobeScreen(paddingValues = paddingValues)
                    }
                    entry<Routes.Settings> {
                        SettingsScreen(paddingValues = paddingValues)
                    }
                })
        }
    }
}