package com.vanced.manager.ui.components.preference

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.vanced.manager.ui.components.dialog.ManagerDialog

@Composable
fun DialogPreference(
    preferenceTitle: String,
    preferenceDescription: String? = null,
    trailing: @Composable () -> Unit = {},
    buttons: @Composable ColumnScope.(isShown: MutableState<Boolean>) -> Unit,
    content: @Composable ColumnScope.() -> Unit
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
                isShown.value = false
            },
            buttons = { buttons(isShown) },
            content = content
        )
    }
}