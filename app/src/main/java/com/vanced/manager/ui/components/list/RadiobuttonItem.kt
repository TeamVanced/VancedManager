package com.vanced.manager.ui.components.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vanced.manager.ui.components.color.managerTextColor
import com.vanced.manager.ui.theme.managerAccentColor

@Composable
fun <T> RadiobuttonItem(
    text: String,
    tag: T,
    selected: Boolean,
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
        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                MaterialTheme.colors.managerAccentColor,
                Color.LightGray
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