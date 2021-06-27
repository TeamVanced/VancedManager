package com.vanced.manager.ui.theme

import androidx.compose.ui.graphics.Color
import com.vanced.manager.ui.preferences.holder.managerAccentColorPref

val accentColor = Color(managerAccentColorPref.value.value)
val accentColorVariant = accentColor.copy(alpha = 0.25f)

val darkOnSurface = Color(0xFFD5D5D5)

val lightOnSurface = Color.Black
val lightSurface = Color(0xFFE5E5E5)

val vancedBlue = Color(0xFF2E73FF)
val vancedRed = Color(0xFFFF0032)