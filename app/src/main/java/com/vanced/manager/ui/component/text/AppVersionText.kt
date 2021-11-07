package com.vanced.manager.ui.component.text

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AppVersionText(
    text: String,
    modifier: Modifier = Modifier,
) {
    ManagerText(
        modifier = modifier,
        text = text,
        textStyle = MaterialTheme.typography.bodySmall,
    )
}