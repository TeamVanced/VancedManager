package com.vanced.manager.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.vanced.manager.R
import com.vanced.manager.core.preferences.managerBooleanPreference
import com.vanced.manager.core.preferences.managerStringPreference

class SettingsViewModel : ViewModel() {

    companion object {
        const val MANAGER_THEME_KEY = "manager_theme"
        const val MANAGER_MODE_KEY = "manager_mode"

        const val THEME_DARK_VALUE = "dark"
        const val THEME_LIGHT_VALUE = "light"
        const val THEME_SYSTEM_DEFAULT_VALUE = "system_default"
    }

    var managerUseCustomTabs by managerBooleanPreference(key = "manager_use_custom_tabs")
    var managerMode by managerStringPreference(key = MANAGER_MODE_KEY, defaultValue = "nonroot")
    var managerTheme by managerStringPreference(MANAGER_THEME_KEY, THEME_SYSTEM_DEFAULT_VALUE)

    fun getThemeStringIdByValue(value: String): Int {
        return when (value) {
            THEME_DARK_VALUE -> R.string.settings_preference_theme_dark
            THEME_LIGHT_VALUE -> R.string.settings_preference_theme_light
            else -> R.string.settings_option_system_default
        }
    }
}