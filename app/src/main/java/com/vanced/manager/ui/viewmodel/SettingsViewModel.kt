package com.vanced.manager.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.vanced.manager.R
import com.vanced.manager.repository.ManagerMode
import com.vanced.manager.repository.ManagerTheme
import com.vanced.manager.repository.PreferenceRepository

class SettingsViewModel(
    private val preferenceRepository: PreferenceRepository
) : ViewModel() {

    var managerUseCustomTabs by mutableStateOf(preferenceRepository.managerUseCustomTabs)
        private set
    var managerMode by mutableStateOf(preferenceRepository.managerMode)
        private set
    var managerTheme by mutableStateOf(preferenceRepository.managerTheme)
        private set

    fun saveManagerUseCustomTabs(value: Boolean) {
        managerUseCustomTabs = value
        preferenceRepository.managerUseCustomTabs = value
    }

    fun saveManagerMode(value: ManagerMode) {
        managerMode = value
        preferenceRepository.managerMode = value
    }

    fun saveManagerTheme(value: ManagerTheme) {
        managerTheme = value
        preferenceRepository.managerTheme = value
    }

    fun getThemeStringId(value: ManagerTheme): Int {
        return when (value) {
            ManagerTheme.DARK -> R.string.settings_preference_theme_dark
            ManagerTheme.LIGHT -> R.string.settings_preference_theme_light
            else -> R.string.settings_option_system_default
        }
    }
}