package com.vanced.manager.ui.component.list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vanced.manager.ui.component.checkbox.ManagerCheckbox
import com.vanced.manager.ui.component.color.managerTextColor
import com.vanced.manager.ui.component.modifier.managerClickable
import com.vanced.manager.ui.widget.checkbox.ManagerAnimatedCheckbox

@Composable
fun CheckboxItem(
    text: String,
    isChecked: Boolean,
    onCheck: (Boolean) -> Unit = {}
) {
    val toggle = { onCheck(!isChecked) }

    ManagerSelectableListItem(
        modifier = Modifier
            .fillMaxWidth()
            .managerClickable(onClick = toggle),
        title = {
            Text(
                text = text,
                color = managerTextColor(),
                fontSize = 18.sp
            )
        },
        trailing = {
            ManagerAnimatedCheckbox(
                size = 24.dp,
                shape = CircleShape,
                isChecked = isChecked
            ) {
                toggle()
            }
        }
    )
}