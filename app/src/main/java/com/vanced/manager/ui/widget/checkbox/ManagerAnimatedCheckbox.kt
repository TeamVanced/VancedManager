package com.vanced.manager.ui.widget.checkbox

import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import com.vanced.manager.ui.component.animation.jumpAnimation
import com.vanced.manager.ui.component.checkbox.ManagerCheckbox
import com.vanced.manager.ui.theme.MediumShape

@Composable
fun ManagerAnimatedCheckbox(
    size: Dp,
    isChecked: Boolean,
    shape: Shape = MediumShape,
    onCheckedChange: (isChecked: Boolean) -> Unit,
) {
    val transition = updateTransition(
        targetState = isChecked,
        label = "Checkbox Animation"
    )
    val animatedSize by transition.jumpAnimation(
        initialValue = size,
        label = "Checkbox Size"
    )
    ManagerCheckbox(
        modifier = Modifier.size(animatedSize),
        isChecked = isChecked,
        shape = shape,
        onCheckedChange = onCheckedChange
    )
}