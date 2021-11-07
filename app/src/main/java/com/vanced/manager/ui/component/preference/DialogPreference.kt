package com.vanced.manager.ui.component.preference

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.vanced.manager.ui.component.dialog.ManagerDialog

@Composable
fun DialogPreference(
    preferenceTitle: String,
    preferenceDescription: String? = null,
    onDismissRequest: () -> Unit = {},
    trailing: @Composable () -> Unit = {},
    confirmButton: @Composable (isShown: MutableState<Boolean>) -> Unit,
    dismissButton: @Composable ((isShown: MutableState<Boolean>) -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val isShown = remember { mutableStateOf(false) }
    Preference(
        preferenceTitle = preferenceTitle,
        preferenceDescription = preferenceDescription,
        trailing = trailing
    ) {
        isShown.value = true
    }
    if (isShown.value) {
        ManagerDialog(
            title = preferenceTitle,
            onDismissRequest = {
                onDismissRequest()
                isShown.value = false
            },
            confirmButton = {
                            confirmButton(isShown)
            },
            dismissButton = {
                if (dismissButton != null) {
                    dismissButton(isShown)
                }
            },
            content = content
        )
    }
}