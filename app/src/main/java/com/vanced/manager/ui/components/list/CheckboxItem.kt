package com.vanced.manager.ui.components.list

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vanced.manager.ui.components.checkbox.ManagerCheckbox
import com.vanced.manager.ui.components.color.managerTextColor
import com.vanced.manager.ui.components.modifier.managerClickable

@Composable
fun CheckboxItem(
    text: String,
    isChecked: Boolean,
    onCheck: (Boolean) -> Unit = {}
) {
    val toggle = { onCheck(!isChecked) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .managerClickable(onClick = toggle)
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(30.dp),
            contentAlignment = Alignment.Center
        ) {
            ManagerCheckbox(
                size = 24.dp,
                isChecked = isChecked
            ) {
                toggle()
            }
        }
        Text(
            modifier = Modifier.padding(start = 12.dp),
            text = text,
            color = managerTextColor(),
            fontSize = 18.sp
        )
    }
}