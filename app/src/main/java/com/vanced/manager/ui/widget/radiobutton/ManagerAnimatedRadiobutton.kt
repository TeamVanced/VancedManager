package com.vanced.manager.ui.widget.radiobutton

import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import com.vanced.manager.ui.component.animation.jumpAnimation
import com.vanced.manager.ui.component.radiobutton.ManagerRadiobutton

@Composable
fun ManagerAnimatedRadiobutton(
    size: Dp,
    isSelected: Boolean,
    shape: Shape = MaterialTheme.shapes.medium,
    onClick: () -> Unit
) {
    val transition = updateTransition(
        targetState = isSelected,
        label = "Radiobutton Animation"
    )
    val animatedSize by transition.jumpAnimation(
        initialValue = size,
        label = "Checkbox Size"
    )
    ManagerRadiobutton(
        modifier = Modifier.size(animatedSize),
        isSelected = isSelected,
        shape = shape,
        onClick = onClick
    )
}