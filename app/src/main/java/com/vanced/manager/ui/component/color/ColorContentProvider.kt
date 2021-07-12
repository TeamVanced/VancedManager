package com.vanced.manager.ui.component.color

import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

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