package com.vanced.manager.ui.component.button

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.vanced.manager.ui.component.color.managerAccentColor
import com.vanced.manager.ui.component.text.ManagerText

@Composable
fun ManagerThemedTextButton(
    modifier: Modifier = Modifier,
    backgroundColor: Color = managerAccentColor(),
    text: String,
    onClick: () -> Unit
) {
    ManagerThemedButton(
        modifier = modifier,
        backgroundColor = backgroundColor,
        onClick = onClick
    ) {
        ManagerText(text = text)
    }
}