package com.vanced.manager.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.vanced.manager.R
import com.vanced.manager.ui.component.layout.ManagerScrollableColumn
import com.vanced.manager.ui.resources.managerString
import com.vanced.manager.ui.util.DefaultContentPaddingVertical
import com.vanced.manager.ui.widget.layout.SettingsCategoryLayout
import com.vanced.manager.ui.widget.screens.settings.*

@Composable
fun SettingsLayout() {
    ManagerScrollableColumn(
        contentPaddingVertical = DefaultContentPaddingVertical,
        itemSpacing = 12.dp
    ) {
        SettingsCategoryLayout(
            categoryName = managerString(
                stringId = R.string.settings_category_behaviour
            )
        ) {
            SettingsCustomTabsItem()
            SettingsNotificationsItem()
            SettingsManagerVariantItem()
        }
        SettingsCategoryLayout(
            categoryName = managerString(
                stringId = R.string.settings_category_appearance
            )
        ) {
            SettingsAccentColorItem()
            ThemeSettingsItem()
        }
    }

}