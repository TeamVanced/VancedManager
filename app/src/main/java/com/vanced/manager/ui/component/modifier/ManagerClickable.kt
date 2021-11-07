package com.vanced.manager.ui.component.modifier

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import com.vanced.manager.ui.theme.MediumShape

fun Modifier.managerClickable(
    onClick: () -> Unit
) = composed {
    val ripple = rememberRipple()
    val interactionSource = remember { MutableInteractionSource() }

    then(clip(MediumShape)).then(
        clickable(
            interactionSource = interactionSource,
            onClick = onClick,
            indication = ripple
        )
    )
}