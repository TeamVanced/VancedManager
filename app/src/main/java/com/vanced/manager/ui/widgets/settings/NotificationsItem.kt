package com.vanced.manager.ui.widgets.settings

import androidx.compose.runtime.Composable
import com.vanced.manager.ui.components.preference.CheckboxPreference
import com.vanced.manager.ui.preferences.managerBooleanPreference
import com.vanced.manager.util.notificationApps

@Composable
fun SettingsNotificationsItem() {
    notificationApps.forEach {
        with(it) {
            CheckboxPreference(
                preferenceTitle = "$app Push Notifications",
                preferenceDescription = "Receive push notifications when an update for $app is released",
                preference = managerBooleanPreference(
                    key = "${prefKey}_notifications",
                    defaultValue = true
                )
            )
        }
    }
}