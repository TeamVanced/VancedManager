package com.vanced.manager.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vanced.manager.R
import com.vanced.manager.ui.component.layout.ManagerLazyColumn
import com.vanced.manager.ui.component.topappbar.ManagerTopAppBar
import com.vanced.manager.ui.resources.managerString
import com.vanced.manager.ui.util.Screen
import com.vanced.manager.ui.widget.layout.managerCategory
import com.vanced.manager.ui.widget.screens.settings.*

@ExperimentalMaterial3Api
@Composable
fun SettingsLayout(
    onToolbarBackButtonClick: () -> Unit,
) {
    val settingsCategoryBehaviour = managerString(R.string.settings_category_behaviour)
    val settingsCategoryApperance = managerString(R.string.settings_category_appearance)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            ManagerTopAppBar(
                title = managerString(Screen.Settings.displayName),
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
            modifier = Modifier.fillMaxSize().padding(paddingValues),
        ) {
            managerCategory(settingsCategoryBehaviour) {
                SettingsCustomTabsItem()
                SettingsNotificationsItem()
                SettingsManagerVariantItem()
            }
            managerCategory(settingsCategoryApperance) {
                SettingsAccentColorItem()
                ThemeSettingsItem()
            }
        }
    }
}