package com.vanced.manager.ui.widget.list

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vanced.manager.ui.component.color.managerTextColor
import com.vanced.manager.ui.component.list.ManagerSelectableListItem
import com.vanced.manager.ui.component.modifier.managerClickable
import com.vanced.manager.ui.component.text.ManagerText
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
            ManagerText(
                text = text,
                color = managerTextColor(),
                textStyle = MaterialTheme.typography.titleSmall
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