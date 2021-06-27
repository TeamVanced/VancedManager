package com.vanced.manager.ui.components.text

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun AppVersionText(
    text: String
) {
    ManagerText(
        text = text,
        textStyle = MaterialTheme.typography.body2,
    )
}