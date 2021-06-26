package com.vanced.manager.ui.widgets.settings

import androidx.compose.runtime.Composable
import com.vanced.manager.R
import com.vanced.manager.ui.components.preference.RadiobuttonDialogPreference
import com.vanced.manager.ui.preferences.RadioButtonPreference
import com.vanced.manager.ui.preferences.holder.managerThemePref

@Composable
fun ThemeSettingsItem() {
    RadiobuttonDialogPreference(
        preferenceTitle = R.string.settings_preference_theme_title,
        preference = managerThemePref,
        buttons = listOf(
            RadioButtonPreference(
                title = "Light Theme",
                key = "Light"
            ),
            RadioButtonPreference(
                title = "Dark Theme",
                key = "Dark"
            ),
            RadioButtonPreference(
                title = "System Default",
                key = "System Default"
            )
        )
    )
}
