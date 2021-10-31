package com.vanced.manager.ui.util

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import com.vanced.manager.R
import com.vanced.manager.ui.screens.AboutLayout
import com.vanced.manager.ui.screens.HomeLayout
import com.vanced.manager.ui.screens.LogLayout
import com.vanced.manager.ui.screens.SettingsLayout

enum class Screen(
    val route: String,
    @StringRes val displayName: Int,
) {
    Home(
        route = "home",
        displayName = R.string.app_name
    ),
    Settings(
        route = "settings",
        displayName = R.string.toolbar_settings,
    ),
    About(
        route = "about",
        displayName = R.string.toolbar_about,
    ),
    Logs(
        route = "logs",
        displayName = R.string.toolbar_logs,
    ),
    InstallPreferences(
        route = "installpreferences",
        displayName = R.string.toolbar_installation_preferences
    ),
    Install(
        route = "install",
        displayName = R.string.toolbar_install
    ),
}
