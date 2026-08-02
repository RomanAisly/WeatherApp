package com.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import com.ui.theme.BaseTheme
import com.ui.theme.lightGray
import com.ui.theme.persianGreen
import com.ui.theme.red
import com.ui.theme.softBlueDark
import com.ui.theme.transparent
import com.weatherapp.ui.R

@Composable
fun BaseAlertDialog(
    onDismissRequest: () -> Unit,
    onCityConfirmed: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var inputText by remember { mutableStateOf("") }

    AlertDialog(
        title = { BaseText(stringResource(R.string.choose_your_city)) },
        text = {
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                singleLine = true,
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
                    unfocusedTextColor = BaseTheme.colors.text,
                    errorBorderColor = red
                )
            )
        },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            BaseTextButton(
                text = "OK",
                textColor = persianGreen,
                onClick = {
                    if (inputText.isNotBlank()) onCityConfirmed(inputText)
                })
        },
        dismissButton = {
            BaseTextButton(
                text = stringResource(R.string.cancel),
                textColor = red,
                onClick = onDismissRequest
            )
        },
        containerColor = transparent,
        icon = { BaseIcon(R.drawable.location) },
        modifier = modifier
            .clip(MaterialTheme.shapes.large)
            .background(BaseTheme.colors.alertBack)
    )
}