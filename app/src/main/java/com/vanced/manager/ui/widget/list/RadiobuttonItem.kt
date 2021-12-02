package com.vanced.manager.ui.widget.list

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vanced.manager.ui.component.color.managerTextColor
import com.vanced.manager.ui.component.list.ManagerSelectableListItem
import com.vanced.manager.ui.component.modifier.managerClickable
import com.vanced.manager.ui.component.text.ManagerText

@Composable
fun <T> RadiobuttonItem(
    text: String,
    tag: T,
    isSelected: Boolean,
    onSelect: (tag: T) -> Unit,
    modifier: Modifier = Modifier
) {
    val onClick = { onSelect(tag) }
    ManagerSelectableListItem(
        modifier = modifier
            .managerClickable(onClick = onClick),
        title = {
            ManagerText(
                text = text,
                color = managerTextColor(),
                textStyle = MaterialTheme.typography.titleSmall
            )
        },
        trailing = {
            RadioButton(
                selected = isSelected,
                onClick = onClick
            )
        }
    )
}