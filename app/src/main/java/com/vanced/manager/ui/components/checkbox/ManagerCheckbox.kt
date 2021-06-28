package com.vanced.manager.ui.components.checkbox

import android.annotation.SuppressLint
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.vanced.manager.ui.components.animation.springAnimation
import com.vanced.manager.ui.components.card.ManagerCard
import com.vanced.manager.ui.components.color.contentColorForColor
import com.vanced.manager.ui.components.color.managerAccentColor
import com.vanced.manager.ui.components.color.managerAnimatedColor
import com.vanced.manager.ui.components.color.managerThemedCardColor

@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun ManagerCheckbox(
    size: Dp,
    isChecked: Boolean,
    onCheckedChange: (isChecked: Boolean) -> Unit
) {
    val accentColor = managerAccentColor()
    val iconInitialSize = size / 1.6f
    val transition = updateTransition(targetState = isChecked, label = "Checked")
    val cardSize by transition.springAnimation(initialValue = size, label = "Checkbox Size")
    val iconSize by transition.springAnimation(initialValue = iconInitialSize, label = "Icon size")
    val cardColor = managerAnimatedColor(if (isChecked) accentColor else managerThemedCardColor())
    val iconTint = managerAnimatedColor(if (isChecked) contentColorForColor(cardColor) else accentColor)

    ManagerCard(
        modifier = Modifier.size(cardSize),
        onClick = { onCheckedChange(!isChecked) },
        backgroundColor = cardColor
    ) {
        Icon(
            modifier = Modifier.requiredSize(iconSize),
            imageVector = if (isChecked) Icons.Rounded.Done else Icons.Rounded.Close,
            tint = iconTint,
            contentDescription = null
        )
    }
}