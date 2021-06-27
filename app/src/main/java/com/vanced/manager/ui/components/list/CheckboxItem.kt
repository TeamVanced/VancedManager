package com.vanced.manager.ui.components.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vanced.manager.ui.components.color.managerAccentColor
import com.vanced.manager.ui.components.color.managerTextColor
import com.vanced.manager.ui.preferences.holder.managerAccentColorPref

@Composable
fun CheckboxItem(
    text: String,
    isChecked: Boolean,
    onCheck: (Boolean) -> Unit = {}
) {
    fun toggle() {
        onCheck(isChecked)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { toggle() }
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = {
                toggle()
            },
            colors = CheckboxDefaults.colors(
                checkedColor = managerAccentColor(),
                uncheckedColor = Color.LightGray
            )
        )
        Text(
            modifier = Modifier.padding(start = 12.dp),
            text = text,
            color = managerTextColor(),
            fontSize = 18.sp
        )
    }
}