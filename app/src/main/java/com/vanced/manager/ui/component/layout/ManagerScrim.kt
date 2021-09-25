package com.vanced.manager.ui.component.layout

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.vanced.manager.ui.theme.isDark

@Composable
fun ManagerScrim(
    show: Boolean
) {
    val systemUiController = rememberSystemUiController()

    val scrimColor = Color.Black.copy(alpha = 0.5f)
    val surfaceColor = MaterialTheme.colors.surface

    val isDark = isDark()

    val scrimAlpha by animateFloatAsState(
        targetValue = if (show) 1f else 0f,
        animationSpec = tween()
    )

    SideEffect {
        //TODO fix colors in navigation bars
        if (show) {
            systemUiController.setSystemBarsColor(scrimColor, false)
        } else {
            systemUiController.setSystemBarsColor(surfaceColor, !isDark)
        }
    }

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