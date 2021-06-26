package com.vanced.manager.ui.components.text

import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.vanced.manager.ui.components.lifecycle.managerString

@Composable
fun ManagerText(
    vararg formatArgs: Any,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    textStyle: TextStyle = LocalTextStyle.current,
    stringId: Int?,
) {
    ManagerText(
        modifier = modifier,
        color = color,
        textStyle = textStyle,
        text = managerString(stringId, *formatArgs),
    )
}

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