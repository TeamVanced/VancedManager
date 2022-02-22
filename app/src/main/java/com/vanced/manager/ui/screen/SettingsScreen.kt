package com.vanced.manager.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vanced.manager.R
import com.vanced.manager.core.util.notificationApps
import com.vanced.manager.ui.component.*
import com.vanced.manager.ui.resource.managerString
import com.vanced.manager.ui.util.Screen
import com.vanced.manager.ui.widget.SettingsCustomTabsItem
import com.vanced.manager.ui.widget.SettingsManagerVariantItem
import com.vanced.manager.ui.widget.SettingsNotificationsItem
import com.vanced.manager.ui.widget.ThemeSettingsItem

@ExperimentalMaterial3Api
@Composable
fun SettingsScreen(
    onToolbarBackButtonClick: () -> Unit,
) {
    ManagerScaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            ManagerSmallTopAppBar(
                title = {
                    ManagerText(managerString(Screen.Settings.displayName))
                },
                navigationIcon = {
                    IconButton(onClick = onToolbarBackButtonClick) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBackIosNew,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        ManagerLazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            managerCategory(categoryName = {
                managerString(R.string.settings_category_behaviour)
            }) {
                item {
                    SettingsCustomTabsItem()
                }
                items(notificationApps) { notificationApp ->
                    SettingsNotificationsItem(notificationApp)
                }
                item {
                    SettingsManagerVariantItem()
                }
            }
            managerCategory(categoryName = {
                managerString(R.string.settings_category_appearance)
            }) {
                item {
                    ThemeSettingsItem()
                }
            }
        }
    }
}