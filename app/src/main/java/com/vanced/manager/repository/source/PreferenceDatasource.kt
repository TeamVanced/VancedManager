package com.vanced.manager.repository.source

import android.content.SharedPreferences

interface PreferenceDatasource {

    var managerUseCustomTabs: Boolean
    var managerMode: String
    var managerTheme: String

}

class PreferenceDatasourceImpl(
    private val sharedPreferences: SharedPreferences
) : PreferenceDatasource {

    override var managerUseCustomTabs: Boolean
        get() = getBoolean(
            PreferenceData.MANAGER_USE_CUSTOM_TABS_KEY,
            PreferenceData.MANAGER_USE_CUSTOM_TABS_DEFAULT_VALUE
        )
        set(value) {
            putBoolean(PreferenceData.MANAGER_USE_CUSTOM_TABS_KEY, value)
        }

    override var managerMode: String
        get() = getString(
            PreferenceData.MANAGER_MODE_KEY,
            PreferenceData.MANAGER_MODE_DEFAULT_VALUE
        )
        set(value) {
            putString(PreferenceData.MANAGER_MODE_KEY, value)
        }

    override var managerTheme: String
        get() = getString(
            PreferenceData.MANAGER_THEME_KEY,
            PreferenceData.MANAGER_THEME_DEFAULT_VALUE
        )
        set(value) {
            putString(PreferenceData.MANAGER_THEME_KEY, value)
        }

    private fun getString(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    private fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    private fun putString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    private fun putBoolean(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }
}

object PreferenceData {

    const val MANAGER_USE_CUSTOM_TABS_KEY = "manager_behaviour_use_custom_tabs"
    const val MANAGER_USE_CUSTOM_TABS_DEFAULT_VALUE = true

    const val MANAGER_MODE_KEY = "manager_behaviour_mode"
    const val MANAGER_MODE_VALUE_ROOT = "root"
    const val MANAGER_MODE_VALUE_NONROOT = "nonroot"
    const val MANAGER_MODE_DEFAULT_VALUE = MANAGER_MODE_VALUE_NONROOT

    const val MANAGER_THEME_KEY = "manager_appearance_theme"
    const val MANAGER_THEME_VALUE_LIGHT = "light"
    const val MANAGER_THEME_VALUE_DARK = "dark"
    const val MANAGER_THEME_VALUE_SYSTEM_DEFAULT = "system_default"
    const val MANAGER_THEME_DEFAULT_VALUE = MANAGER_THEME_VALUE_SYSTEM_DEFAULT

}