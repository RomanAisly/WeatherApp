package com.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import com.domain.CityItem
import com.ui.theme.BaseTheme
import com.ui.theme.lightGray
import com.ui.theme.softBlueDark
import com.weatherapp.ui.R
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun BaseAlertDialog(
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    searchResults: List<CityItem>,

    onDismissRequest: () -> Unit,
    onCityConfirmed: (CityItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }
    var startDialogAnimation by remember { mutableStateOf(false) }

    var cachedResults by remember { mutableStateOf(searchResults) }
    LaunchedEffect(searchResults) {
        if (searchResults.isNotEmpty()) {
            cachedResults = searchResults
        }
    }

    LaunchedEffect(Unit) {
        startDialogAnimation = true
        delay(250.milliseconds)
        focusRequester.requestFocus()
    }

    Dialog(onDismissRequest = onDismissRequest) {
        AnimatedVisibility(
            startDialogAnimation,
            enter = scaleIn(tween(450)) + fadeIn(
                tween(500)
            ),
            exit = scaleOut(tween(450)) + fadeOut(
                tween(500)
            )
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.large)
                    .background(BaseTheme.colors.alertBack)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    BaseIcon(R.drawable.location, iconTint = BaseTheme.colors.text)
                    BaseText(
                        text = stringResource(R.string.choose_your_city),
                        textStyle = MaterialTheme.typography.titleLarge
                    )
                }
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onQueryChange,
                    singleLine = true,
                    placeholder = { BaseText(stringResource(R.string.search_city)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                        .zIndex(1f),
                    shape = CircleShape,
                    textStyle = TextStyle(
                        color = BaseTheme.colors.text,
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = softBlueDark,
                        unfocusedBorderColor = lightGray,
                        cursorColor = softBlueDark,
                        focusedTextColor = BaseTheme.colors.text,
                        unfocusedTextColor = BaseTheme.colors.text
                    )
                )
                AnimatedVisibility(
                    visible = searchResults.isNotEmpty(),
                    enter = slideInVertically(
                        tween(500)
                    ) { -it } + fadeIn(
                        tween(550)
                    ),
                    exit = slideOutVertically(
                        tween(500)
                    ) { -it } + fadeOut(
                        tween(550)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 250.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        cachedResults.forEach { city ->
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
                                            textStyle = MaterialTheme.typography.titleLarge
                                        )
                                        BaseText(
                                            text = city.country,
                                            textStyle = MaterialTheme.typography.titleSmall,
                                        )
                                        HorizontalDivider(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(top = 4.dp),
                                            color = BaseTheme.colors.text.copy(alpha = 0.5f)
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
            }
        }
    }
}