package com.vanced.manager.ui.widget.settings

import androidx.compose.runtime.Composable

@Composable
fun SettingsAccentColorItem() {
//    var localAccentColor by remember { mutableStateOf(managerAccentColorPref.value.value) }
//    DialogPreference(
//        preferenceTitle = managerString(
//            stringId = R.string.settings_preference_accent_color_title
//        ),
//        preferenceDescription = "#" + Integer.toHexString(localAccentColor.toInt()),
//        buttons = { isShown ->
//            ManagerResetButton(
//                backgroundColor = Color(localAccentColor)
//            ) {
//                isShown.value = false
//                managerAccentColorPref.save(defAccentColor)
//            }
//            ManagerSaveButton(
//                backgroundColor = Color(localAccentColor)
//            ) {
//                isShown.value = false
//                managerAccentColorPref.save(localAccentColor)
//            }
//        }
//    ) {
//        ManagerColorPicker {
//            localAccentColor = it
//        }
//    }
}