package com.vanced.manager.ui.components.preference

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.vanced.manager.R
import com.vanced.manager.ui.components.button.ManagerThemedTextButton
import com.vanced.manager.ui.components.list.CheckboxItem
import com.vanced.manager.ui.preferences.CheckboxPreference
import com.vanced.manager.ui.preferences.ManagerPreference
import kotlinx.coroutines.launch

@Composable
fun CheckboxDialogPreference(
    preferenceTitle: String,
    preference: ManagerPreference<Set<String>>,
    trailing: @Composable () -> Unit = {},
    buttons: List<CheckboxPreference>,
    onSave: (checkedButtons: List<String>) -> Unit = {}
) {
    var pref by preference
    val selectedButtons = remember { pref.toMutableStateList() }
    val coroutineScope = rememberCoroutineScope()
    DialogPreference(
        preferenceTitle = preferenceTitle,
        preferenceDescription = buttons.filter { button ->
            selectedButtons.any { selectedButton ->
                button.key == selectedButton
            }
        }.joinToString(separator = ", ") { it.title },
        trailing = trailing,
        buttons = { isShown ->
            ManagerThemedTextButton(
                text = stringResource(id = R.string.dialog_button_save),
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    coroutineScope.launch {
                        isShown.value = false
                        pref = selectedButtons.toSet()
                        onSave(selectedButtons)
                    }
                }
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
        ) {
            items(buttons) { button ->
                val (title, key) = button
                CheckboxItem(
                    text = title,
                    isChecked = selectedButtons.contains(key),
                    onCheck = { isChecked ->
                        if (isChecked) {
                            selectedButtons.add(key)
                        } else {
                            selectedButtons.remove(key)
                        }
                    }
                )
            }
        }
    }
}