package com.vanced.manager.ui.components.progressindicator

import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.Composable
import com.vanced.manager.ui.components.color.managerAccentColor

@Composable
fun ManagerProgressIndicator() {
    LinearProgressIndicator(
        color = managerAccentColor()
    )
}

@Composable
fun ManagerProgressIndicator(
    progress: Float
) {
    LinearProgressIndicator(
        color = managerAccentColor(),
        progress = progress
    )
}