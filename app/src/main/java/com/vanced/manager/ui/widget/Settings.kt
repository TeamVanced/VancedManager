package com.vanced.manager.ui.widget

import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import com.vanced.manager.R
import com.vanced.manager.core.preferences.RadioButtonPreference
import com.vanced.manager.core.preferences.holder.managerThemePref
import com.vanced.manager.core.preferences.holder.managerVariantPref
import com.vanced.manager.core.preferences.holder.useCustomTabsPref
import com.vanced.manager.core.preferences.managerBooleanPreference
import com.vanced.manager.core.util.isMagiskInstalled
import com.vanced.manager.domain.model.NotificationPrefModel
import com.vanced.manager.ui.component.ManagerPreference
import com.vanced.manager.ui.component.ManagerSingleSelectDialogPreference
import com.vanced.manager.ui.component.ManagerSwitchPreference
import com.vanced.manager.ui.resource.managerString

@Composable
fun SettingsClearFilesItem() {
    ManagerPreference(
        preferenceTitle = managerString(
            stringId = R.string.settings_preference_clear_files_title
        ),
        preferenceDescription = null,
        onClick = {}
    )
}

@Composable
fun SettingsCustomTabsItem() {
    ManagerSwitchPreference(
        preferenceTitle = stringResource(id = R.string.settings_preference_use_custom_tabs_title),
        preferenceDescription = stringResource(id = R.string.settings_preference_use_custom_tabs_summary),
        isChecked = useCustomTabsPref,
        onCheckedChange = {
            useCustomTabsPref = it
        }
    )
}

@Composable
fun SettingsManagerVariantItem() {
    var showDialog by remember { mutableStateOf(false) }
    var selectedKey by remember { mutableStateOf(managerVariantPref) }
    ManagerSingleSelectDialogPreference(
        preferenceTitle = managerString(
            stringId = R.string.settings_preference_variant_title
        ),
        preferenceDescription = managerVariantPref,
        isDialogVisible = showDialog,
        currentSelectedKey = selectedKey,
        buttons = listOf(
            RadioButtonPreference(
                title = "nonroot",
                key = "nonroot"
            ),
            RadioButtonPreference(
                title = "root",
                key = "root"
            ),
        ),
        onPreferenceClick = {
            showDialog = true
        },
        onDismissRequest = {
            showDialog = false
            selectedKey = managerVariantPref
        },
        onItemClick = {
            if (it == "root" && !isMagiskInstalled)
                return@ManagerSingleSelectDialogPreference

            selectedKey = it
        },
        onSave = {
            managerVariantPref = selectedKey
            showDialog = false
        }
    )
}

@Composable
fun SettingsNotificationsItem(notificationApp: NotificationPrefModel) {
    with(notificationApp) {
        var appNotificationsPref by managerBooleanPreference(
            key = "${prefKey}_notifications",
            defaultValue = true
        )
        ManagerSwitchPreference(
            preferenceTitle = "$app Push Notifications",
            preferenceDescription = "Receive push notifications when an update for $app is released",
            isChecked = appNotificationsPref,
            onCheckedChange = {
                appNotificationsPref = it
            }
        )
    }
}

@Composable
fun ThemeSettingsItem() {
    var showDialog by remember { mutableStateOf(false) }
    var selectedKey by remember { mutableStateOf(managerThemePref) }
    ManagerSingleSelectDialogPreference(
        preferenceTitle = managerString(stringId = R.string.settings_preference_theme_title),
        preferenceDescription = managerThemePref,
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
            selectedKey = managerThemePref
        },
        onItemClick = {
            selectedKey = it
        },
        onSave = {
            managerThemePref = selectedKey
            showDialog = false
        }
    )
}