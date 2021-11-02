package com.vanced.manager.ui.util

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import com.vanced.manager.R
import com.vanced.manager.domain.model.InstallationOption
import com.vanced.manager.ui.screens.AboutLayout
import com.vanced.manager.ui.screens.HomeLayout
import com.vanced.manager.ui.screens.LogLayout
import com.vanced.manager.ui.screens.SettingsLayout

sealed class Screen(
    val route: String,
    @StringRes val displayName: Int,
) {
    object Home : Screen(
        route = "home",
        displayName = R.string.app_name
    )

    object Settings: Screen(
        route = "settings",
        displayName = R.string.toolbar_settings,
    )

    object About: Screen(
        route = "about",
        displayName = R.string.toolbar_about,
    )
    object Logs : Screen(
        route = "logs",
        displayName = R.string.toolbar_logs,
    )
    data class InstallPreferences(
        val appName: String,
        val appVersions: List<String>?,
        val appInstallationOptions: List<InstallationOption>
    ) : Screen(
        route = "installpreferences",
        displayName = R.string.toolbar_installation_preferences
    )

    data class Install(
        val appName: String,
        val appVersions: List<String>?
    ) : Screen(
        route = "install",
        displayName = R.string.toolbar_install
    )
}
