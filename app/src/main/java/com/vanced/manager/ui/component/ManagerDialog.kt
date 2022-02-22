package com.vanced.manager.ui.component

import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vanced.manager.ui.theme.LargeShape

@Composable
fun ManagerDialog(
    title: String,
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    dismissButton: @Composable (() -> Unit)? = null,
    icon: @Composable (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    AlertDialog(
        modifier = modifier,
        title = {
            ManagerText(
                text = title,
                textAlign = TextAlign.Center
            )
        },
        text = content,
        onDismissRequest = onDismissRequest,
        confirmButton = confirmButton,
        dismissButton = dismissButton,
        icon = icon,
        shape = LargeShape,
        tonalElevation = 2.dp,
    )
}