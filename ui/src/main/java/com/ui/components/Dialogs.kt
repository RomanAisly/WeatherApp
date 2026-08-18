package com.ui.components

import android.content.Intent
import android.provider.Settings
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import com.domain.models.CityItem
import com.ui.theme.BaseTheme
import com.ui.theme.lightGray
import com.ui.theme.red
import com.ui.theme.softBlueDark
import com.ui.theme.transparent
import com.ui.theme.white
import com.weatherapp.ui.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@Composable
fun GpsWarningDialog(
    onGoToSettings: () -> Unit,
    onSearchManually: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onSearchManually,
        title = {
            BaseText(
                text = stringResource(R.string.geolocation_is_disabled),
                textStyle = MaterialTheme.typography.bodyLarge
            )
        },
        text = {
            BaseText(
                text = stringResource(R.string.please_turn_on_gps)
            )
        },
        confirmButton = {
            BaseTextButton(
                text = stringResource(R.string.turn_on),
                onClick = {
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    context.startActivity(intent)
                    onGoToSettings()
                }
            )

        },
        dismissButton = {
            BaseTextButton(
                text = stringResource(R.string.search_manually),
                onClick = {
                    onSearchManually()
                }
            )
        },
        containerColor = transparent,
        modifier = modifier
            .clip(MaterialTheme.shapes.large)
            .background(BaseTheme.colors.alertBack)
    )
}

@Composable
fun CitySearchOverlay(
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    searchResults: List<CityItem>,
    layoutMode: LayoutMode,
    onDismissRequest: () -> Unit,
    onCityConfirmed: (CityItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }
    var startDialogAnimation by remember { mutableStateOf(false) }
    var cachedResults by remember { mutableStateOf(searchResults) }

    LaunchedEffect(searchResults) {
        if (searchResults.isNotEmpty()) cachedResults = searchResults
    }

    LaunchedEffect(Unit) {
        startDialogAnimation = true
        delay(250.milliseconds)
        focusRequester.requestFocus()
    }

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onDismissRequest
                )
                .padding(14.dp)
                .imePadding(),
            contentAlignment = if (layoutMode == LayoutMode.LANDSCAPE_PHONE) Alignment.TopStart else Alignment.TopCenter
        ) {
            val isLandscape = layoutMode == LayoutMode.LANDSCAPE_PHONE
            val searchWidth = if (isLandscape) maxWidth * 0.45f else maxWidth
            val resultsWidth = maxWidth * 0.40f

            val targetWidth by animateDpAsState(
                targetValue = if (isLandscape) {
                    if (searchResults.isNotEmpty()) {
                        searchWidth + 16.dp + resultsWidth + 40.dp
                    } else {
                        searchWidth + 40.dp
                    }
                } else {
                    maxWidth
                },
                animationSpec = tween(500)
            )

            AnimatedVisibility(
                visible = startDialogAnimation,
                enter = scaleIn(tween(450)) + fadeIn(tween(500)),
                exit = scaleOut(tween(450)) + fadeOut(tween(500))
            ) {
                Box(
                    modifier = modifier
                        .padding(
                            top = if (isLandscape) 6.dp else 80.dp,
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 16.dp
                        )
                        .width(targetWidth)
                        .clip(MaterialTheme.shapes.large)
                        .background(BaseTheme.colors.alertBack)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = {}
                        )
                        .animateContentSize(tween(500))
                        .padding(20.dp)
                ) {
                    if (isLandscape) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.width(searchWidth),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                DialogHeader()
                                SearchInput(searchQuery, onQueryChange, focusRequester)
                            }

                            AnimatedVisibility(
                                visible = searchResults.isNotEmpty(),
                                enter = slideInHorizontally(tween(500)) { -it / 2 } + fadeIn(
                                    tween(
                                        500
                                    )
                                ),
                                exit = slideOutHorizontally(tween(500)) { -it / 2 } + fadeOut(
                                    tween(
                                        500
                                    )
                                )
                            ) {
                                SearchResultsList(
                                    results = cachedResults,
                                    onCityConfirmed = onCityConfirmed,
                                    modifier = Modifier.width(resultsWidth)
                                )
                            }
                        }
                    } else {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            DialogHeader()
                            SearchInput(searchQuery, onQueryChange, focusRequester)

                            AnimatedVisibility(
                                visible = searchResults.isNotEmpty(),
                                enter = slideInVertically(tween(500)) { -it } + fadeIn(tween(550)),
                                exit = slideOutVertically(tween(500)) { -it } + fadeOut(tween(550))
                            ) {
                                SearchResultsList(cachedResults, onCityConfirmed)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DialogHeader() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        BaseIcon(R.drawable.location, iconTint = white)
        BaseText(
            text = stringResource(R.string.choose_your_city),
            textStyle = MaterialTheme.typography.titleLarge,
            textColor = white
        )
    }
}

@Composable
private fun SearchInput(
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    focusRequester: FocusRequester
) {
    OutlinedTextField(
        value = searchQuery,
        onValueChange = onQueryChange,
        singleLine = true,
        placeholder = { BaseText(stringResource(R.string.search_city), textColor = white) },
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .zIndex(1f),
        shape = CircleShape,
        textStyle = TextStyle(
            color = white,
            fontSize = MaterialTheme.typography.bodyLarge.fontSize
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = white,
            unfocusedBorderColor = lightGray,
            cursorColor = softBlueDark,
            focusedTextColor = BaseTheme.colors.text,
            unfocusedTextColor = BaseTheme.colors.text
        )
    )
}

@Composable
private fun SearchResultsList(
    results: List<CityItem>,
    onCityConfirmed: (CityItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = 250.dp)
            .verticalScroll(rememberScrollState())
    ) {
        results.forEach { city ->
            key(city.id) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.medium)
                        .clickable { onCityConfirmed(city) }
                        .padding(vertical = 12.dp, horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        BaseText(
                            text = city.name,
                            textStyle = MaterialTheme.typography.titleLarge,
                            textColor = white
                        )
                        BaseText(
                            text = city.country,
                            textStyle = MaterialTheme.typography.titleSmall,
                            textColor = white
                        )
                        HorizontalDivider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp),
                            color = white.copy(alpha = 0.7f)
                        )
                    }
                    BaseText(
                        city.flagEmoji,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun <T> SnackBarFlow(
    snackFlow: Flow<T>,
    messageRes: (T) -> Int,
    modifier: Modifier = Modifier
) {
    var isVisible by remember { mutableStateOf(false) }
    var displayItem by remember { mutableStateOf<T?>(null) }

    val animDuration = 500
    val fadeDuration = 550

    LaunchedEffect(snackFlow) {
        snackFlow.collectLatest { item ->
            displayItem = item
            isVisible = true
            delay(5.seconds)
            isVisible = false
        }
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(
            animationSpec = tween(animDuration),
            initialOffsetY = { it }
        ) + fadeIn(animationSpec = tween(fadeDuration)),
        exit = slideOutVertically(
            animationSpec = tween(animDuration),
            targetOffsetY = { it }
        ) + fadeOut(animationSpec = tween(fadeDuration)),
        modifier = modifier
    ) {
        displayItem?.let { item ->
            BaseCard(
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .padding(horizontal = 18.dp)
                    .fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                containerColor = BaseTheme.colors.bgCenter
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BaseIcon(iconId = R.drawable.error, iconTint = red)
                    Spacer(modifier = Modifier.width(12.dp))
                    BaseText(
                        text = stringResource(messageRes(item)),
                        modifier = Modifier.weight(1f)
                    )
                    BaseIconButton(
                        modifier = Modifier.padding(end = 8.dp),
                        iconId = R.drawable.close,
                        iconTint = BaseTheme.colors.text,
                        onClick = { isVisible = false }
                    )
                }
            }
        }
    }
}