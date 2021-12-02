package com.vanced.manager.ui.widget.list

import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vanced.manager.ui.component.color.managerTextColor
import com.vanced.manager.ui.component.list.ManagerSelectableListItem
import com.vanced.manager.ui.component.modifier.managerClickable
import com.vanced.manager.ui.component.text.ManagerText

@Composable
fun CheckboxItem(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    ManagerSelectableListItem(
        modifier = modifier
            .managerClickable(onClick = {
                onCheckedChange(!checked)
            }),
        title = {
            ManagerText(
                text = text,
                color = managerTextColor(),
                textStyle = MaterialTheme.typography.titleSmall
            )
        },
        trailing = {
            Checkbox(
                checked = checked,
                onCheckedChange = null
            )
        }
    )
}