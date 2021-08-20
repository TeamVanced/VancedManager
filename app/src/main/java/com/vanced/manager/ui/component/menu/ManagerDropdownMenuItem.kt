package com.vanced.manager.ui.component.menu

import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import com.vanced.manager.ui.component.text.ManagerText

@Composable
fun ManagerDropdownMenuItem(
    title: String,
    onClick: () -> Unit
) {
    DropdownMenuItem(
        onClick = onClick,
    ) {
        ManagerText(
            text = title,
            textStyle = MaterialTheme.typography.h6
        )
    }
}