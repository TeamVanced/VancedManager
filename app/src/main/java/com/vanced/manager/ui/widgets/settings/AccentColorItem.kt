package com.vanced.manager.ui.widgets.settings

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import com.vanced.manager.R
import com.vanced.manager.ui.components.color.ManagerColorPicker
import com.vanced.manager.ui.components.preference.DialogPreference
import com.vanced.manager.ui.preferences.holder.managerAccentColorPref
import com.vanced.manager.ui.resources.managerString
import com.vanced.manager.ui.widgets.button.ManagerSaveButton

@Composable
fun SettingsAccentColorItem() {
    var localAccentColor by remember { mutableStateOf(managerAccentColorPref.value.value) }
    DialogPreference(
        preferenceTitle = managerString(
            stringId = R.string.settings_preference_accent_color_title
        ),
        preferenceDescription = "#" + Integer.toHexString(localAccentColor.toInt()),
        buttons = { isShown ->
            ManagerSaveButton(
                backgroundColor = Color(localAccentColor)
            ) {
                isShown.value = false
                managerAccentColorPref.save(localAccentColor)
            }
        }
    ) {
        ManagerColorPicker {
            localAccentColor = it
        }
    }
}