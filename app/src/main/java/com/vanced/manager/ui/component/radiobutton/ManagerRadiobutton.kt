package com.vanced.manager.ui.component.radiobutton

import android.annotation.SuppressLint
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import com.vanced.manager.ui.component.card.ManagerCard
import com.vanced.manager.ui.component.color.managerAccentColor
import com.vanced.manager.ui.component.color.managerAnimatedColor
import com.vanced.manager.ui.component.color.managerThemedCardColor

@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun ManagerRadiobutton(
    modifier: Modifier,
    isSelected: Boolean,
    shape: Shape = MaterialTheme.shapes.medium,
    onClick: () -> Unit
) {
    val accentColor = managerAccentColor()
    val cardColor = managerAnimatedColor(if (isSelected) accentColor else managerThemedCardColor())
    ManagerCard(
        modifier = modifier,
        onClick = onClick,
        shape = shape,
        backgroundColor = cardColor,
        content = {}
    )
}