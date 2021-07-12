package com.vanced.manager.ui.widget.screens.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.vanced.manager.R
import com.vanced.manager.ui.component.preference.CheckboxPreference
import com.vanced.manager.preferences.holder.useCustomTabsPref

@Composable
fun SettingsCustomTabsItem() {
    CheckboxPreference(
        preferenceTitle = stringResource(id = R.string.settings_preference_use_custom_tabs_title),
        preferenceDescription = stringResource(id = R.string.settings_preference_use_custom_tabs_summary),
        preference = useCustomTabsPref
    )
}