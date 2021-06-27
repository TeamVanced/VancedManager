package com.vanced.manager.ui.widgets.settings

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import com.vanced.manager.R
import com.vanced.manager.ui.components.button.ManagerThemedTextButton
import com.vanced.manager.ui.components.color.ManagerColorPicker
import com.vanced.manager.ui.components.preference.DialogPreference
import com.vanced.manager.ui.preferences.holder.managerAccentColorPref

@Composable
fun SettingsAccentColorItem() {
    var localAccentColor by remember { mutableStateOf(managerAccentColorPref.value.value) }
    DialogPreference(
        preferenceTitleId = R.string.settings_preference_accent_color_title,
        preferenceDescription = "#" + Integer.toHexString(localAccentColor.toInt()),
        buttons = { isShown ->
            ManagerThemedTextButton(
                stringId = R.string.dialog_button_save,
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