package com.vanced.manager.ui.components.checkbox

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.keyframes
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
import androidx.compose.ui.unit.dp
import com.vanced.manager.ui.components.card.ManagerCard
import com.vanced.manager.ui.components.color.contentColorForColor
import com.vanced.manager.ui.components.color.managerAccentColor
import com.vanced.manager.ui.components.color.managerAnimatedColor
import com.vanced.manager.ui.components.color.managerThemedCardColor

@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun ManagerCheckbox(
    isChecked: Boolean,
    onCheckedChange: (isChecked: Boolean) -> Unit
) {
    val accentColor = managerAccentColor()
    val transition = updateTransition(targetState = isChecked, label = "Checked")
    val cardSize by transition.animateDp(
        transitionSpec = {
             keyframes {
                 durationMillis = 250
                 32.dp at 50
                 48.dp at 150
                 40.dp at 250
             }
        },
        label = "Checkbox size"
    ) { 40.dp }
    val iconSize by transition.animateDp(
        transitionSpec = {
            keyframes {
                durationMillis = 250
                18.dp at 50
                30.dp at 150
                24.dp at 250
            }
        },
        label = "Icon size"
    ) { 24.dp }
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