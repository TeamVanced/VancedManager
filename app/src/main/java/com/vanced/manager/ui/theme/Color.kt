package com.vanced.manager.ui.theme

import androidx.compose.ui.graphics.Color
import com.vanced.manager.ui.preferences.holder.managerAccentColorPref

val accentColor = Color(managerAccentColorPref.value.value)
val accentColorVariant = accentColor.copy(alpha = 0.25f)

val lightOnSurface = Color(0xFFD7D7D7)
val lightSurface = Color(0xFFE9E9E9)

val vancedBlue = Color(0xFF2E73FF)
val vancedRed = Color(0xFFFF0032)