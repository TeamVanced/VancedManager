package com.vanced.manager.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

var managerTheme by mutableStateOf<String?>("")

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

val Colors.managerAccentColor: Color get() = Color(0xFF2E73FF)

@Composable
fun isDark(): Boolean = when (managerTheme) {
    "Dark" -> true
    "Light" -> false
    "System Default" -> isSystemInDarkTheme()
    else -> throw IllegalArgumentException("Unknown theme")
}

@Composable
fun ComposeTestTheme(
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