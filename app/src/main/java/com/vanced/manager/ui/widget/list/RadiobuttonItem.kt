package com.vanced.manager.ui.widget.list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vanced.manager.ui.component.color.managerTextColor
import com.vanced.manager.ui.component.list.ManagerSelectableListItem
import com.vanced.manager.ui.component.modifier.managerClickable
import com.vanced.manager.ui.widget.radiobutton.ManagerAnimatedRadiobutton

@Composable
fun <T> RadiobuttonItem(
    text: String,
    tag: T,
    isSelected: Boolean,
    onSelect: (tag: T) -> Unit
) {
    val onClick = { onSelect(tag) }
    ManagerSelectableListItem(
        modifier = Modifier
            .fillMaxWidth()
            .managerClickable(onClick = onClick),
        title = {
            Text(
                text = text,
                color = managerTextColor(),
                fontSize = 18.sp
            )
        },
        trailing = {
            ManagerAnimatedRadiobutton(
                size = 24.dp,
                shape = CircleShape,
                isSelected = isSelected,
                onClick = onClick
            )
        }
    )
}