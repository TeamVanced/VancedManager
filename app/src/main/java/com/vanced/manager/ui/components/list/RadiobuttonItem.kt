package com.vanced.manager.ui.components.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vanced.manager.ui.components.color.managerTextColor
import com.vanced.manager.ui.components.radiobutton.ManagerRadiobutton

@Composable
fun <T> RadiobuttonItem(
    text: String,
    tag: T,
    isSelected: Boolean,
    onSelect: (tag: T) -> Unit
) {
    val onClick = {
        onSelect(tag)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(30.dp),
            contentAlignment = Alignment.Center
        ) {
            ManagerRadiobutton(
                size = 24.dp,
                isSelected = isSelected,
                onClick = onClick
            )
        }
        Text(
            modifier = Modifier.padding(start = 12.dp),
            text = text,
            color = managerTextColor(),
            fontSize = 18.sp
        )
    }
}