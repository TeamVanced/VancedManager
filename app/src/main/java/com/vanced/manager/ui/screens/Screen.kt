package com.vanced.manager.ui.screens

sealed class Screen(val route: String, val displayName: String) {
    object Home : Screen("home", "Manager")
    object Settings : Screen("settings", "Settings")
    object About : Screen("about", "About")
    object Logs : Screen("logs", "Logs")
}
