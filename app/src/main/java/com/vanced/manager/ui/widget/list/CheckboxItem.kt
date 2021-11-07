package com.vanced.manager.ui.widget.list

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vanced.manager.ui.component.color.managerTextColor
import com.vanced.manager.ui.component.list.ManagerSelectableListItem
import com.vanced.manager.ui.component.modifier.managerClickable
import com.vanced.manager.ui.widget.checkbox.ManagerAnimatedCheckbox

@Composable
fun CheckboxItem(
    text: String,
    isChecked: Boolean,
    onCheck: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val toggle = { onCheck(!isChecked) }

    ManagerSelectableListItem(
        modifier = modifier
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