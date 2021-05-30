package com.vanced.manager.ui.layouts

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vanced.manager.ui.composables.*
import com.vanced.manager.ui.preferences.RadioButtonPreference
import com.vanced.manager.ui.theme.managerTheme

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

@ExperimentalStdlibApi
@ExperimentalMaterialApi
@Composable
fun SettingsLayout() {
    var showDialog by remember { mutableStateOf(false) }
    ManagerScrollableColumn {
        HeaderCard(headerName = "Behaviour") {
            SwitchPreference(
                preferenceTitle = "Use Custom Tabs",
                preferenceDescription = "Links will open in chrome custom tabs",
                preferenceKey = "use_custom_tabs"
            )
            notificationApps.forEach {
                with (it) {
                    SwitchPreference(
                        preferenceTitle = "$app Push Notifications",
                        preferenceDescription = "Receive push notifications when an update for $app is released",
                        preferenceKey = "${prefKey}_notifications"
                    )
                }
            }
            Preference(
                preferenceTitle = "Variant",
                preferenceDescription = "nonroot",
                onClick = {}
            )
            Preference(
                preferenceTitle = "Clear downloaded files",
                onClick = {}
            )
        }
        Spacer(modifier = Modifier.size(12.dp))
        HeaderCard(headerName = "Appearance") {
            Preference(
                preferenceTitle = "Accent Color",
                onClick = {
                    showDialog = true
                }
            )
            DialogRadioButtonPreference(
                preferenceTitle = "Theme",
                preferenceKey = "manager_theme",
                defaultValue = "Light",
                buttons = listOf(
                    RadioButtonPreference(
                        title = "Light Theme",
                        preferenceValue = "Light"
                    ),
                    RadioButtonPreference(
                        title = "Dark Theme",
                        preferenceValue = "Dark"
                    ),
                    RadioButtonPreference(
                        title = "System Default",
                        preferenceValue = "System Default"
                    )
                )
            ) {
                managerTheme = it
            }
        }
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            text = {
                HSLColorPicker()
//                    AndroidView(
//                        factory = {
//                            HSLColorPicker(it)
//                        },
//                        update = { view ->
//                            view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 800)
//                            view.setColorSelectionListener(object : SimpleColorSelectionListener() {
//                                override fun onColorSelected(color: Int) {
//                                    accentColorInt = color
//                                }
//                            })
//                        }
//                    )
            },
            buttons = {}
        )
    }

}