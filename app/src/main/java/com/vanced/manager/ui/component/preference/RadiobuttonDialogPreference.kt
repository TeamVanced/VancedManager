package com.vanced.manager.ui.component.preference

import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vanced.manager.R
import com.vanced.manager.core.preferences.RadioButtonPreference
import com.vanced.manager.ui.component.text.ManagerText
import com.vanced.manager.ui.resources.managerString
import com.vanced.manager.ui.widget.list.RadiobuttonItem

@Composable
fun RadiobuttonDialogPreference(
    preferenceTitle: String,
    preferenceDescription: String,
    isDialogVisible: Boolean,
    currentSelectedKey: String,
    buttons: List<RadioButtonPreference>,
    trailing: @Composable () -> Unit = {},
    onPreferenceClick: () -> Unit,
    onDismissRequest: () -> Unit,
    onItemClick: (itemKey: String) -> Unit,
    onSave: () -> Unit,
) {
    DialogPreference(
        preferenceTitle = preferenceTitle,
        preferenceDescription = preferenceDescription,
        trailing = trailing,
        confirmButton = {
            TextButton(onClick = onSave) {
                ManagerText(managerString(R.string.dialog_button_save))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                ManagerText(managerString(R.string.dialog_button_cancel))
            }
        },
        onDismissRequest = onDismissRequest,
        isDialogVisible = isDialogVisible,
        onPreferenceClick = onPreferenceClick
    ) {
        LazyColumn(
            modifier = Modifier.heightIn(max = 400.dp)
        ) {
            items(buttons) { button ->
                val (title, key) = button
                RadiobuttonItem(
                    text = title,
                    tag = key,
                    isSelected = currentSelectedKey == key,
                    onSelect = onItemClick
                )
            }
        }
    }
}