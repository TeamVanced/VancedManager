package com.vanced.manager.ui.components.preference

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vanced.manager.R
import com.vanced.manager.ui.components.button.ManagerThemedTextButton
import com.vanced.manager.ui.components.list.RadiobuttonItem
import com.vanced.manager.ui.preferences.ManagerPreference
import com.vanced.manager.ui.preferences.RadioButtonPreference
import kotlinx.coroutines.launch

@Composable
fun RadiobuttonDialogPreference(
    @StringRes preferenceTitle: Int,
    preference: ManagerPreference<String>,
    trailing: @Composable () -> Unit = {},
    buttons: List<RadioButtonPreference>,
    onSave: (newPref: String?) -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()
    var currentSelection by remember { mutableStateOf(preference.value.value) }
    DialogPreference(
        preferenceTitleId = preferenceTitle,
        preferenceDescription = currentSelection,
        trailing = trailing,
        buttons = { isShown ->
            ManagerThemedTextButton(
                stringId = R.string.dialog_button_save,
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    coroutineScope.launch {
                        preference.save(currentSelection)
                        onSave(currentSelection)
                        isShown.value = false
                    }
                }
            )
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
                    selected = currentSelection == key,
                    onSelect = {
                        currentSelection = it
                    }
                )
            }
        }
    }
}