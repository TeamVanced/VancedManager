package com.vanced.manager.ui.widgets.text

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import com.vanced.manager.ui.components.text.ManagerText

@Composable
fun ToolbarTitleText(
    stringId: Int?
) {
    ManagerText(
        stringId = stringId,
        textStyle = MaterialTheme.typography.h1
    )
}