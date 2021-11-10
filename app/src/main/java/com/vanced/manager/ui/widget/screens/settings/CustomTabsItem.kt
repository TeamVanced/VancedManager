package com.vanced.manager.ui.widget.screens.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.vanced.manager.R
import com.vanced.manager.core.preferences.holder.useCustomTabsPref
import com.vanced.manager.ui.component.preference.CheckboxPreference

@Composable
fun SettingsCustomTabsItem() {
    CheckboxPreference(
        preferenceTitle = stringResource(id = R.string.settings_preference_use_custom_tabs_title),
        preferenceDescription = stringResource(id = R.string.settings_preference_use_custom_tabs_summary),
        isChecked = useCustomTabsPref,
        onCheckedChange = {
            useCustomTabsPref = it
        }
    )
}