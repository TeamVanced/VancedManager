package com.vanced.manager.ui.component.preference

import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vanced.manager.ui.component.list.RadiobuttonItem
import com.vanced.manager.preferences.ManagerPreference
import com.vanced.manager.preferences.RadioButtonPreference
import com.vanced.manager.ui.widget.button.ManagerSaveButton
import kotlinx.coroutines.launch

@Composable
fun RadiobuttonDialogPreference(
    preferenceTitle: String,
    preference: ManagerPreference<String>,
    trailing: @Composable () -> Unit = {},
    buttons: List<RadioButtonPreference>,
    onSave: (newPref: String?) -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()
    var currentSelection by remember { mutableStateOf(preference.value.value) }
    DialogPreference(
        preferenceTitle = preferenceTitle,
        preferenceDescription = buttons.find { it.key == currentSelection }?.title,
        trailing = trailing,
        buttons = { isShown ->
            ManagerSaveButton {
                coroutineScope.launch {
                    isShown.value = false
                    preference.save(currentSelection)
                    onSave(currentSelection)
                }
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier.heightIn(max = 400.dp)
        ) {
            items(buttons) { button ->
                val (title, key) = button
                RadiobuttonItem(
                    text = title,
                    tag = key,
                    isSelected = currentSelection == key,
                    onSelect = {
                        currentSelection = it
                    }
                )
            }
        }
    }
}