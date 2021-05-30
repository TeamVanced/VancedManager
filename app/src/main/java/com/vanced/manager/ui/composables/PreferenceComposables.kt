package com.vanced.manager.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.vanced.manager.ui.preferences.RadioButtonPreference

@Composable
fun SwitchPreference(
    preferenceTitle: String,
    preferenceDescription: String?,
    preferenceKey: String,
    defaultValue: Boolean = true,
    onCheckedChange: (isChecked: Boolean) -> Unit = {}
) {
    val prefs = PreferenceManager.getDefaultSharedPreferences(LocalContext.current)
    var isChecked by remember { mutableStateOf(prefs.getBoolean(preferenceKey, defaultValue)) }

    fun savePreference() {
        isChecked = !isChecked
        prefs.edit {
            putBoolean(preferenceKey, isChecked)
        }
        onCheckedChange(isChecked)
    }

    Preference(
        preferenceTitle = preferenceTitle,
        preferenceDescription = preferenceDescription,
        onClick = { savePreference() },
        trailing = {
            Switch(
                checked = isChecked,
                onCheckedChange = { savePreference() },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = managerAccentColor(),
                    checkedTrackColor = managerAccentColor(),
                    uncheckedThumbColor = Color.Gray,
                    uncheckedTrackColor = Color.Gray
                )
            )
        }
    )
}

@Composable
fun Preference(
    preferenceTitle: String,
    preferenceDescription: String? = null,
    trailing: @Composable (() -> Unit)? = null,
    onClick: () -> Unit
) {
    ManagerListItem(
        modifier = Modifier.clickable(onClick = onClick),
        title = preferenceTitle,
        description = preferenceDescription,
        trailing = trailing,
        bottomPadding = 4.dp,
        topPadding = 4.dp
    )
}

@Composable
fun DialogPreference(
    preferenceTitle: String,
    preferenceDescription: String? = null,
    trailing: @Composable (() -> Unit)? = null,
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
            isShown = isShown,
            buttons = { buttons(isShown) },
            content = content
        )
    }
}

@Composable
fun DialogRadioButtonPreference(
    preferenceTitle: String,
    preferenceKey: String,
    defaultValue: String,
    preferenceDescription: String? = null,
    trailing: @Composable (() -> Unit)? = null,
    buttons: List<RadioButtonPreference>,
    onSave: (newPref: String?) -> Unit = {}
) {
    val prefs = PreferenceManager.getDefaultSharedPreferences(LocalContext.current)
    val currentSelection = remember { mutableStateOf(prefs.getString(preferenceKey, defaultValue)) }
    DialogPreference(
        preferenceTitle = preferenceTitle,
        preferenceDescription = preferenceDescription,
        trailing = trailing,
        buttons = { isShown ->
            ManagerThemedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    prefs.edit {
                        putString(preferenceKey, currentSelection.value)
                    }
                    onSave(currentSelection.value)
                    isShown.value = false
                }
            ) {
                Text(text = "Save")
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(
                    weight = 1f,
                    fill = false
                )

        ) {
            items(buttons) { button ->
                val (title, key) = button
                RadiobuttonItem(
                    currentSelection = currentSelection,
                    text = title,
                    preferenceValue = key
                )
            }
        }
    }
}