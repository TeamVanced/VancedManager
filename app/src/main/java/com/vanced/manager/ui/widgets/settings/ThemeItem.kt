package com.vanced.manager.ui.widgets.settings

import androidx.compose.runtime.Composable
import com.vanced.manager.R
import com.vanced.manager.ui.components.preference.RadiobuttonDialogPreference
import com.vanced.manager.ui.preferences.RadioButtonPreference
import com.vanced.manager.ui.preferences.holder.managerThemePref
import com.vanced.manager.ui.resources.managerString

@Composable
fun ThemeSettingsItem() {
    val lightTheme = managerString(stringId = R.string.settings_preference_theme_light)
    val darkTheme = managerString(stringId = R.string.settings_preference_theme_dark)
    val sysDefTheme = managerString(stringId = R.string.settings_option_system_default)

    val lightKey = "Light"
    val darkKey = "Dark"
    val sysDefKey = "System Default"

    RadiobuttonDialogPreference(
        preferenceTitle = managerString(stringId = R.string.settings_preference_theme_title),
        preference = managerThemePref,
        buttons = listOf(
            RadioButtonPreference(
                title = lightTheme,
                key = lightKey
            ),
            RadioButtonPreference(
                title = darkTheme,
                key = darkKey
            ),
            RadioButtonPreference(
                title = sysDefTheme,
                key = sysDefKey
            )
        ),
        preferenceDescriptionConverter = {
            when (it) {
                lightKey -> lightTheme
                darkKey -> darkTheme
                else -> sysDefTheme
            }
        }
    )
}
