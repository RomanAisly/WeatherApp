package com.ui.components

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigationevent.compose.LocalNavigationEventDispatcherOwner
import androidx.navigationevent.compose.rememberNavigationEventDispatcherOwner
import com.ui.navigation.BottomNavGraph
import com.ui.theme.WeatherTheme
import org.koin.compose.KoinContext
import org.koin.dsl.koinApplication

enum class LayoutMode {
    PORTRAIT,
    LANDSCAPE_PHONE,
    FOLD_TABLET
}

@Composable
@Preview(
    name = "Light Mode",
    showBackground = true,
    showSystemUi = true
)
@Preview(
    name = "Dark Mode",
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
internal fun UiToolsPreview() {
    val koin = remember {
        koinApplication {
            modules(previewModule)
        }.koin
    }
    val navigationEventDispatcherOwner = rememberNavigationEventDispatcherOwner(parent = null)
    KoinContext(context = koin) {
        CompositionLocalProvider(LocalNavigationEventDispatcherOwner provides navigationEventDispatcherOwner) {
            WeatherTheme(onThemeChange = {}) {
                BottomNavGraph()
            }
        }
    }
}