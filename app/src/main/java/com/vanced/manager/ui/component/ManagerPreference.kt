package com.vanced.manager.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Checkbox
import androidx.compose.material.Switch
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.vanced.manager.R
import com.vanced.manager.core.preferences.RadioButtonPreference
import com.vanced.manager.ui.resource.managerString
import com.vanced.manager.ui.theme.LargeShape
import com.vanced.manager.ui.theme.SmallShape
import com.vanced.manager.ui.util.DefaultContentPaddingHorizontal

@Composable
fun ManagerPreference(
    preferenceTitle: String,
    preferenceDescription: String? = null,
    trailing: @Composable () -> Unit = {},
    onClick: () -> Unit
) {
    ManagerElevatedCard(
        shape = LargeShape,
        onClick = onClick
    ) {
        ManagerListItem(
            modifier = Modifier
                .padding(horizontal = DefaultContentPaddingHorizontal),
            title = {
                ManagerText(text = preferenceTitle)
            },
            description = if (preferenceDescription != null) {
                {
                    ManagerText(text = preferenceDescription)
                }
            } else null,
            trailing = trailing,
        )
    }
}

@Composable
fun ManagerSwitchPreference(
    preferenceTitle: String,
    preferenceDescription: String? = null,
    isChecked: Boolean,
    onCheckedChange: (isChecked: Boolean) -> Unit
) {
    ManagerPreference(
        preferenceTitle = preferenceTitle,
        preferenceDescription = preferenceDescription,
        onClick = {
            onCheckedChange(!isChecked)
        },
        trailing = {
            Switch(
                checked = isChecked,
                onCheckedChange = null
            )
        }
    )
}

@Composable
fun ManagerDialogPreference(
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
    ManagerPreference(
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

@Composable
fun ManagerSingleSelectDialogPreference(
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
    ManagerDialogPreference(
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
                ListDialogRadiobuttonItem(
                    modifier = Modifier.fillMaxWidth(),
                    text = title,
                    selected = currentSelectedKey == key,
                    onClick = {
                        onItemClick(key)
                    }
                )
            }
        }
    }
}

@Composable
fun ManagerMultiSelectDialogPreference(
    preferenceTitle: String,
    preferenceDescription: String,
    isDialogVisible: Boolean,
    currentSelectedKeys: List<String>,
    buttons: List<RadioButtonPreference>,
    trailing: @Composable () -> Unit = {},
    onPreferenceClick: () -> Unit,
    onDismissRequest: () -> Unit,
    onItemCheckChange: (isChecked: Boolean, itemKey: String) -> Unit,
    onSave: () -> Unit,
) {
    ManagerDialogPreference(
        preferenceTitle = preferenceTitle,
        preferenceDescription = preferenceDescription,
        trailing = trailing,
        confirmButton = {
            androidx.compose.material.TextButton(onClick = onSave) {
                ManagerText(managerString(R.string.dialog_button_save))
            }
        },
        dismissButton = {
            androidx.compose.material.TextButton(onClick = onDismissRequest) {
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
                ListDialogCheckboxItem(
                    text = title,
                    checked = currentSelectedKeys.contains(key),
                    onCheckedChange = { isChecked ->
                        onItemCheckChange(isChecked, key)
                    }
                )
            }
        }
    }
}

@Composable
private fun ListDialogRadiobuttonItem(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ListDialogItem(
        modifier = modifier,
        text = text, 
        onClick = onClick,
        trailing = {
            RadioButton(
                selected = selected,
                onClick = null
            )
        }
    )
}

@Composable
private fun ListDialogCheckboxItem(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    ListDialogItem(
        modifier = modifier,
        text = text, 
        onClick = {
            onCheckedChange(!checked)
        },
        trailing = {
            Checkbox(
                checked = checked,
                onCheckedChange = null
            )
        }
    )
}

@Composable
private fun ListDialogItem(
    text: String,
    onClick: () -> Unit,
    trailing: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(SmallShape)
            .clickable(onClick = onClick)
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ManagerText(
            modifier = Modifier.weight(1f),
            text = text,
            textStyle = MaterialTheme.typography.titleSmall
        )
        trailing()
    }
}