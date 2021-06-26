package com.vanced.manager.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.vanced.manager.ui.preferences.holder.managerAccentColorPref
import com.vanced.manager.ui.preferences.holder.managerThemePref

const val defAccentColor = 0xFF0477E1

private val DarkColorPalette = darkColors(
    primary = purple200,
    primaryVariant = purple700,
    secondary = teal200
)

private val LightColorPalette = lightColors(
    primary = purple500,
    primaryVariant = purple700,
    secondary = teal200,
    surface = Color(0xFFE9E9E9)
)

val Colors.cardColor: Color get() = if (isLight) Color(0xFFF7F7F7) else Color(0xFF191919)

val Colors.managerAccentColor: Color get() = Color(managerAccentColorPref.value.value)

@Composable
fun isDark(): Boolean = when (managerThemePref.value.value) {
    "Dark" -> true
    "Light" -> false
    "System Default" -> isSystemInDarkTheme()
    else -> throw IllegalArgumentException("Unknown theme")
}

@Composable
fun ManagerTheme(
    content: @Composable () -> Unit
) {
    val colors = if (isDark()) DarkColorPalette else LightColorPalette

    MaterialTheme(
        colors = colors,
        typography = typography,
        shapes = shapes,
        content = content
    )
}