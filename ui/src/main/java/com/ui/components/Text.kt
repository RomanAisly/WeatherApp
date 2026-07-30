package com.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.ui.theme.BaseTheme

@Composable
fun BaseText(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    textColor: Color = BaseTheme.colors.text,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    overflow: TextOverflow = TextOverflow.Ellipsis,
) {
    Text(
        modifier = modifier,
        text = text,
        style = textStyle,
        color = textColor,
        maxLines = maxLines,
        minLines = minLines,
        overflow = overflow,
        textAlign = textAlign
    )
}