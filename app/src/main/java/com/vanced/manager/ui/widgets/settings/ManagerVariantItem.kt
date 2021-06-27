package com.vanced.manager.ui.widgets.settings

import androidx.compose.runtime.Composable
import com.vanced.manager.R
import com.vanced.manager.ui.components.preference.RadiobuttonDialogPreference
import com.vanced.manager.ui.preferences.RadioButtonPreference
import com.vanced.manager.ui.preferences.holder.managerVariantPref
import com.vanced.manager.ui.resources.managerString

@Composable
fun SettingsManagerVariantItem() {
    RadiobuttonDialogPreference(
        preferenceTitle = managerString(
            stringId = R.string.settings_preference_variant_title
        ),
        preference = managerVariantPref,
        buttons = listOf(
            RadioButtonPreference(
                title = "nonroot",
                key = "nonroot"
            ),
            RadioButtonPreference(
                title = "root",
                key = "root"
            ),
        )
    )
}