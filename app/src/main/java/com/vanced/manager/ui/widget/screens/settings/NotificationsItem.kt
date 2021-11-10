package com.vanced.manager.ui.widget.screens.settings

import androidx.compose.runtime.Composable
import com.vanced.manager.core.preferences.managerBooleanPreference
import com.vanced.manager.core.util.notificationApps
import com.vanced.manager.ui.component.preference.CheckboxPreference

@Composable
fun SettingsNotificationsItem() {
    notificationApps.forEach { notificationApp ->
        with(notificationApp) {
            var appNotificationsPref by managerBooleanPreference(
                key = "${prefKey}_notifications",
                defaultValue = true
            )
            CheckboxPreference(
                preferenceTitle = "$app Push Notifications",
                preferenceDescription = "Receive push notifications when an update for $app is released",
                isChecked = appNotificationsPref,
                onCheckedChange = {
                    appNotificationsPref = it
                }
            )
        }
    }
}