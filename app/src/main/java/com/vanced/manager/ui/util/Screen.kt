package com.vanced.manager.ui.util

import androidx.annotation.StringRes
import com.vanced.manager.R
import com.vanced.manager.domain.model.InstallationOption

sealed class Screen(
    val route: String,
    @StringRes val displayName: Int,
) {
    object Home : Screen(
        route = "home",
        displayName = R.string.app_name
    )

    object Settings : Screen(
        route = "settings",
        displayName = R.string.toolbar_settings,
    )

    object About : Screen(
        route = "about",
        displayName = R.string.toolbar_about,
    )

    object Logs : Screen(
        route = "logs",
        displayName = R.string.toolbar_logs,
    )

    data class Configuration(
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
