package com.vanced.manager.ui.components.text

import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

@Composable
fun ManagerText(
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    textStyle: TextStyle = LocalTextStyle.current,
    text: String,
) {
    Text(
        modifier = modifier,
        text = text,
        color = color,
        style = textStyle
    )
}