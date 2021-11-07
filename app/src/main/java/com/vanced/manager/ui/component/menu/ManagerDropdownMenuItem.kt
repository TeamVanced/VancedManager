package com.vanced.manager.ui.component.menu

import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.vanced.manager.ui.component.text.ManagerText
import com.vanced.manager.ui.theme.SmallShape

@Composable
fun ManagerDropdownMenuItem(
    title: String,
    onClick: () -> Unit
) {
    DropdownMenuItem(
        onClick = onClick,
        modifier = Modifier.clip(SmallShape),
    ) {
        ManagerText(
            text = title,
            textStyle = MaterialTheme.typography.labelLarge
        )
    }
}