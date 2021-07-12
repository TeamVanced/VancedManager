package com.vanced.manager.ui.component.radiobutton

import android.annotation.SuppressLint
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import com.vanced.manager.ui.component.animation.springAnimation
import com.vanced.manager.ui.component.card.ManagerCard
import com.vanced.manager.ui.component.color.managerAccentColor
import com.vanced.manager.ui.component.color.managerAnimatedColor
import com.vanced.manager.ui.component.color.managerThemedCardColor

@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun ManagerRadiobutton(
    size: Dp,
    isSelected: Boolean,
    shape: Shape = MaterialTheme.shapes.medium,
    onClick: () -> Unit
) {
    val accentColor = managerAccentColor()
    val transition = updateTransition(targetState = isSelected, label = "Selected")
    val cardSize by transition.springAnimation(initialValue = size, label = "Radiobutton Size")
    val cardColor = managerAnimatedColor(if (isSelected) accentColor else managerThemedCardColor())
    ManagerCard(
        modifier = Modifier.size(cardSize),
        onClick = onClick,
        shape = shape,
        backgroundColor = cardColor,
        content = {}
    )
}