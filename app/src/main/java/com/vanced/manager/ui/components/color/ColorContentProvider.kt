package com.vanced.manager.ui.components.color

import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun ThemedItemContentColorProvider(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalContentColor provides contentColorForColor(MaterialTheme.colors.primary),
        content = content
    )
}

@Composable
fun ThemedCardContentColorProvider(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalContentColor provides contentColorForColor(MaterialTheme.colors.primaryVariant),
        content = content
    )
}

@Composable
fun ThemedContentColorProvider(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalContentColor provides MaterialTheme.colors.primary,
        content = content
    )
}