package com.vanced.manager.ui.widget.screens.settings

import androidx.compose.runtime.*
import com.vanced.manager.R
import com.vanced.manager.core.preferences.RadioButtonPreference
import com.vanced.manager.core.preferences.holder.managerThemePref
import com.vanced.manager.ui.component.preference.RadiobuttonDialogPreference
import com.vanced.manager.ui.resources.managerString

@Composable
fun ThemeSettingsItem() {
    var showDialog by remember { mutableStateOf(false) }
    var selectedKey by remember { mutableStateOf(managerThemePref.value.value) }
    RadiobuttonDialogPreference(
        preferenceTitle = managerString(stringId = R.string.settings_preference_theme_title),
        preferenceDescription = managerThemePref.value.value,
        isDialogVisible = showDialog,
        currentSelectedKey = selectedKey,
        buttons = listOf(
            RadioButtonPreference(
                title = managerString(R.string.settings_preference_theme_light),
                key = "Light"
            ),
            RadioButtonPreference(
                title = managerString(R.string.settings_preference_theme_dark),
                key = "Dark"
            ),
            RadioButtonPreference(
                title = managerString(R.string.settings_option_system_default),
                key = "System Default"
            )
        ),
        onPreferenceClick = {
            showDialog = true
        },
        onDismissRequest = {
            showDialog = false
            selectedKey = managerThemePref.value.value
        },
        onItemClick = {
            selectedKey = it
        },
        onSave = {
            managerThemePref.save(selectedKey)
            showDialog = false
        }
    )
}
