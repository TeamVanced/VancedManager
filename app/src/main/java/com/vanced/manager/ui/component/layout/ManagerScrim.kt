package com.vanced.manager.ui.component.layout

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier

@Composable
fun ManagerScrim(
    show: Boolean
) {
    val scrimAlpha by animateFloatAsState(
        targetValue = if (show) 1f else 0f,
        animationSpec = tween()
    )

    val scrimColor = MaterialTheme.colors.onSurface.copy(alpha = 0.32f)

    Canvas(
        Modifier
            .fillMaxSize()
    ) {
        drawRect(
            color = scrimColor,
            alpha = scrimAlpha
        )
    }
}