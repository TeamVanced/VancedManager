package com.vanced.manager.repository

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import com.vanced.manager.repository.source.PreferenceData
import com.vanced.manager.repository.source.PreferenceDatasource

interface PreferenceRepository {

    var managerUseCustomTabs: Boolean
    var managerMode: ManagerMode
    var managerTheme: ManagerTheme

}

class PreferenceRepositoryImpl(
    private val preferenceDatasource: PreferenceDatasource
) : PreferenceRepository {

    override var managerUseCustomTabs: Boolean
        get() = preferenceDatasource.managerUseCustomTabs
        set(value) {
            preferenceDatasource.managerUseCustomTabs = value
        }

    override var managerMode: ManagerMode
        get() = ManagerMode.fromValue(preferenceDatasource.managerMode)
        set(value) {
            preferenceDatasource.managerMode = value.value
        }

    override var managerTheme: ManagerTheme
        get() = ManagerTheme.fromValue(preferenceDatasource.managerTheme)
        set(value) {
            preferenceDatasource.managerTheme = value.value
        }

}


enum class ManagerTheme(val value: String) {
    LIGHT(PreferenceData.MANAGER_THEME_VALUE_LIGHT),
    DARK(PreferenceData.MANAGER_THEME_VALUE_DARK),
    SYSTEM_DEFAULT(PreferenceData.MANAGER_THEME_VALUE_SYSTEM_DEFAULT);

    @Composable
    fun isDark() = when (this) {
        LIGHT -> false
        DARK -> true
        SYSTEM_DEFAULT -> isSystemInDarkTheme()
    }

    companion object {
        fun fromValue(value: String?): ManagerTheme {
            return values().find {
                it.value == value
            } ?: SYSTEM_DEFAULT
        }
    }
}

enum class ManagerMode(val value: String) {
    ROOT(PreferenceData.MANAGER_MODE_VALUE_ROOT),
    NONROOT(PreferenceData.MANAGER_MODE_VALUE_NONROOT);

    val isRoot get() = this == ROOT
    val isNonroot get() = this == NONROOT

    companion object {
        fun fromValue(value: String?): ManagerMode {
            return when (value) {
                "root" -> ROOT
                else -> NONROOT
            }
        }
    }
}