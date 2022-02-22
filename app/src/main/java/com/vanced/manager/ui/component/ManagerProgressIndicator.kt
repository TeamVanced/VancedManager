package com.vanced.manager.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.material.ProgressIndicatorDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.vanced.manager.ui.util.animated

@Composable
fun ManagerProgressIndicator(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    trackColor: Color = MaterialTheme.colorScheme.surfaceVariant,
) {
    LinearProgressIndicator(
        modifier = modifier,
        color = color.animated,
        trackColor = trackColor.animated
    )
}

@Composable
fun ManagerProgressIndicator(
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    trackColor: Color = MaterialTheme.colorScheme.surfaceVariant,
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    )
    LinearProgressIndicator(
        progress = animatedProgress,
        modifier = modifier,
        color = color.animated,
        trackColor = trackColor.animated
    )
}
