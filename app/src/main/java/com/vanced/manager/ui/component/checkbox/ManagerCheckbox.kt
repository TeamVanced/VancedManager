package com.vanced.manager.ui.component.checkbox

import android.annotation.SuppressLint
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import com.vanced.manager.ui.component.animation.springAnimation
import com.vanced.manager.ui.component.card.ManagerCard
import com.vanced.manager.ui.component.color.contentColorForColor
import com.vanced.manager.ui.component.color.managerAccentColor
import com.vanced.manager.ui.component.color.managerAnimatedColor
import com.vanced.manager.ui.component.color.managerThemedCardColor
import com.vanced.manager.util.log

@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun ManagerCheckbox(
    size: Dp,
    isChecked: Boolean,
    shape: Shape = MaterialTheme.shapes.medium,
    onCheckedChange: (isChecked: Boolean) -> Unit
) {
    val transition = updateTransition(targetState = isChecked, label = "Checked")
    val cardSize by transition.springAnimation(initialValue = size, label = "Checkbox Size")
    val iconSize = cardSize / 1.6f
    val cardColor = managerAnimatedColor(if (isChecked) managerAccentColor() else managerThemedCardColor())
    val iconTint = managerAnimatedColor(if (isChecked) contentColorForColor(cardColor) else managerAccentColor())
    
    ManagerCard(
        modifier = Modifier.size(cardSize),
        shape = shape,
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