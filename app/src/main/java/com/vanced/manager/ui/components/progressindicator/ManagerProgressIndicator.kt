package com.vanced.manager.ui.components.progressindicator

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import com.vanced.manager.ui.components.color.managerAccentColor

private val progressBarModifier = Modifier.composed {
    then(
        fillMaxWidth()
    ).then(
        clip(MaterialTheme.shapes.medium)
    )
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