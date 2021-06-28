package com.vanced.manager.ui.layouts

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.vanced.manager.R
import com.vanced.manager.ui.components.layout.ManagerScrollableColumn
import com.vanced.manager.ui.resources.managerString
import com.vanced.manager.ui.utils.defaultContentPaddingVertical
import com.vanced.manager.ui.widgets.layout.SettingsCategoryLayout
import com.vanced.manager.ui.widgets.settings.*

@Composable
fun SettingsLayout() {
    ManagerScrollableColumn(
        contentPaddingVertical = defaultContentPaddingVertical,
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