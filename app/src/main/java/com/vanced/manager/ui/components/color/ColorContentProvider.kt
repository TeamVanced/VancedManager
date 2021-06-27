package com.vanced.manager.ui.components.color

import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import com.vanced.manager.ui.preferences.holder.managerAccentColorPref

@Composable
fun ThemedItemContentColorProvider(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalContentColor provides contentColorForColor(managerAccentColor()),
        content = content
    )
}

@Composable
fun ThemedCardContentColorProvider(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalContentColor provides contentColorForColor(managerAccentColor()),
        content = content
    )
}

@Composable
fun ThemedContentColorProvider(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalContentColor provides managerAccentColor(),
        content = content
    )
}