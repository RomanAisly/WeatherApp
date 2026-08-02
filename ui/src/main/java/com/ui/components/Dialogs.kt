package com.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.domain.CityItem
import com.ui.theme.BaseTheme
import com.ui.theme.gray
import com.ui.theme.lightGray
import com.ui.theme.softBlueDark
import com.weatherapp.ui.R

@Composable
fun BaseAlertDialog(
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    searchResults: List<CityItem>,

    onDismissRequest: () -> Unit,
    onCityConfirmed: (CityItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(onDismissRequest = onDismissRequest) {
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
                placeholder = {
                    BaseText(
                        stringResource(R.string.search_city),
                        textStyle = MaterialTheme.typography.bodySmall,
                        textColor = gray
                    )
                },
                modifier = Modifier.fillMaxWidth(),
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
            AnimatedVisibility(visible = searchResults.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 250.dp)
                ) {
                    items(searchResults) { city ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(MaterialTheme.shapes.medium)
                                .clickable { onCityConfirmed(city) }
                                .padding(vertical = 12.dp, horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                BaseText(
                                    text = city.name,
                                    textStyle = MaterialTheme.typography.bodyLarge
                                )
                                BaseText(
                                    text = city.country,
                                    textStyle = MaterialTheme.typography.bodySmall,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}