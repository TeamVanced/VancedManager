package com.vanced.manager.ui.components.button

import androidx.annotation.StringRes
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.vanced.manager.ui.components.color.managerAccentColor
import com.vanced.manager.ui.components.text.ManagerText
import com.vanced.manager.ui.preferences.holder.managerAccentColorPref

@Composable
fun ManagerThemedTextButton(
    modifier: Modifier = Modifier,
    backgroundColor: Color = managerAccentColor(),
    @StringRes stringId: Int,
    onClick: () -> Unit
) {
    ManagerThemedButton(
        modifier = modifier,
        backgroundColor = backgroundColor,
        onClick = onClick
    ) {
        ManagerText(stringId = stringId)
    }
}