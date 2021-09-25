package com.vanced.manager.ui.component.checkbox

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Done
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import com.vanced.manager.ui.component.card.ManagerCard
import com.vanced.manager.ui.component.color.contentColorForColor
import com.vanced.manager.ui.component.color.managerAccentColor
import com.vanced.manager.ui.component.color.managerAnimatedColor
import com.vanced.manager.ui.component.color.managerThemedCardColor

@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun ManagerCheckbox(
    modifier: Modifier,
    isChecked: Boolean,
    shape: Shape = MaterialTheme.shapes.medium,
    onCheckedChange: (isChecked: Boolean) -> Unit
) {
    val cardColor =
        managerAnimatedColor(if (isChecked) managerAccentColor() else managerThemedCardColor())
    val iconTint =
        managerAnimatedColor(if (isChecked) contentColorForColor(cardColor) else managerAccentColor())

    ManagerCard(
        modifier = modifier,
        shape = shape,
        onClick = { onCheckedChange(!isChecked) },
        backgroundColor = cardColor
    ) {
        BoxWithConstraints {
            Icon(
                modifier = Modifier
                    .requiredSizeIn(
                        minWidth = minWidth / 1.6f,
                        minHeight = minHeight / 1.6f,
                        maxWidth = maxWidth / 1.6f,
                        maxHeight = maxHeight / 1.6f,
                    )
                    .align(Alignment.Center),
                imageVector = if (isChecked) Icons.Rounded.Done else Icons.Rounded.Close,
                tint = iconTint,
                contentDescription = null
            )
        }
    }
}