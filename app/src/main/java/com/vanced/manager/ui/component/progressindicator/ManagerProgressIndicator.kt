package com.vanced.manager.ui.component.progressindicator

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.vanced.manager.ui.component.color.managerAccentColor

private val progressBarModifier = Modifier.composed {
    then(height(5.dp))
        .then(fillMaxWidth())
        .then(clip(MaterialTheme.shapes.medium))
}

@Composable
fun ManagerProgressIndicator() {
    LinearProgressIndicator(
        modifier = progressBarModifier,
        color = managerAccentColor()
    )
}

@Composable
fun ManagerProgressIndicator(
    progress: Float
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    )
    LinearProgressIndicator(
        modifier = progressBarModifier,
        color = managerAccentColor(),
        progress = animatedProgress
    )
}