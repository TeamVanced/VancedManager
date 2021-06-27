package com.vanced.manager.ui.layouts

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import com.vanced.manager.R
import com.vanced.manager.ui.components.layout.ManagerScrollableColumn
import com.vanced.manager.ui.components.preference.CheckboxPreference
import com.vanced.manager.ui.components.preference.Preference
import com.vanced.manager.ui.components.preference.RadiobuttonDialogPreference
import com.vanced.manager.ui.preferences.RadioButtonPreference
import com.vanced.manager.ui.preferences.holder.managerVariantPref
import com.vanced.manager.ui.preferences.holder.useCustomTabsPref
import com.vanced.manager.ui.preferences.managerBooleanPreference
import com.vanced.manager.ui.utils.defaultContentPaddingVertical
import com.vanced.manager.ui.widgets.layout.CategoryLayout
import com.vanced.manager.ui.widgets.settings.SettingsAccentColorItem
import com.vanced.manager.ui.widgets.settings.ThemeSettingsItem

data class NotificationPrefModel(
    val app: String,
    val prefKey: String
)

private val notificationApps = arrayOf(
    NotificationPrefModel(
        app = "YouTube Vanced",
        prefKey = "vanced"
    ),
    NotificationPrefModel(
        app = "YouTube Music Vanced",
        prefKey = "music"
    ),
    NotificationPrefModel(
        app = "Vanced microG",
        prefKey = "microg"
    )
)

@Composable
fun SettingsLayout() {
    ManagerScrollableColumn(
        contentPaddingVertical = defaultContentPaddingVertical,
        itemSpacing = 12.dp
    ) {
        CategoryLayout(
            categoryNameId = R.string.settings_category_behaviour,
            contentPaddingHorizontal = 0.dp,
            categoryNameSpacing = 4.dp
        ) {
            Column {
                CheckboxPreference(
                    preferenceTitle = R.string.settings_preference_use_custom_tabs_title,
                    preferenceDescription = R.string.settings_preference_use_custom_tabs_summary,
                    preference = useCustomTabsPref
                )
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
                RadiobuttonDialogPreference(
                    preferenceTitle = R.string.settings_preference_variant_title,
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
                Preference(
                    preferenceTitleId = R.string.settings_preference_clear_files_title,
                    preferenceDescriptionId = null,
                    onClick = {}
                )
            }
        }
        CategoryLayout(
            categoryNameId = R.string.settings_category_appearance,
            contentPaddingHorizontal = 0.dp,
            categoryNameSpacing = 4.dp
        ) {
            Column {
                SettingsAccentColorItem()
                ThemeSettingsItem()
            }
        }
    }

}