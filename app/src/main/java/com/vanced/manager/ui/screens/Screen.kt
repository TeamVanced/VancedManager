package com.vanced.manager.ui.screens

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import com.vanced.manager.R
import com.vanced.manager.ui.layouts.AboutLayout
import com.vanced.manager.ui.layouts.HomeLayout
import com.vanced.manager.ui.layouts.LogLayout
import com.vanced.manager.ui.layouts.SettingsLayout

sealed class Screen(
    val route: String,
    @StringRes val displayName: Int,
    val content: @Composable () -> Unit
) {

    object Home : Screen(
        route = "home",
        displayName = R.string.toolbar_home,
        content = {
            HomeLayout()
        }
    )

    object Settings : Screen(
        route = "settings",
        displayName = R.string.toolbar_settings,
        content = {
            SettingsLayout()
        }
    )

    object About : Screen(
        route = "about",
        displayName = R.string.toolbar_about,
        content = {
            AboutLayout()
        }
    )

    object Logs : Screen(
        route = "logs",
        displayName = R.string.toolbar_logs,
        content = {
            LogLayout()
        }
    )
}
