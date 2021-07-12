package com.vanced.manager.ui.component.modifier

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip

fun Modifier.managerClickable(
    onClick: () -> Unit
) = composed {
    val ripple = rememberRipple()
    val interactionSource = remember { MutableInteractionSource() }

    return@composed then(
        clip(MaterialTheme.shapes.medium)
    ).then(
        clickable(
            interactionSource = interactionSource,
            onClick = onClick,
            indication = ripple
        )
    )
}