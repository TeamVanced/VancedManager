package com.vanced.manager.ui.component.preference

import androidx.compose.runtime.Composable
import com.vanced.manager.ui.component.dialog.ManagerDialog

@Composable
fun DialogPreference(
    preferenceTitle: String,
    preferenceDescription: String? = null,
    trailing: @Composable () -> Unit = {},
    onPreferenceClick: () -> Unit,
    isDialogVisible: Boolean,
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit,
    dismissButton: @Composable () -> Unit = {},
    content: @Composable () -> Unit
) {
    Preference(
        preferenceTitle = preferenceTitle,
        preferenceDescription = preferenceDescription,
        trailing = trailing,
        onClick = onPreferenceClick
    )
    if (isDialogVisible) {
        ManagerDialog(
            title = preferenceTitle,
            onDismissRequest = onDismissRequest,
            confirmButton = confirmButton,
            dismissButton = dismissButton,
            content = content
        )
    }
}