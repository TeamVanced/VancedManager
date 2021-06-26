package com.vanced.manager.ui.components.color

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import com.vanced.manager.ui.preferences.holder.managerAccentColorPref
import com.vanced.manager.ui.theme.cardColor

fun managerAccentColor(): Color = Color(managerAccentColorPref.value.value)

@Composable
fun contentColorForColor(color: Color) =
    if (color.luminance() > 0.7)
        Color.Black
    else
        Color.White

@Composable
fun managerThemedCardColor() = managerAccentColor().copy(alpha = 0.2f)

@Composable
fun managerTextColor(): Color = managerAnimatedColor(color = MaterialTheme.colors.onSurface)

@Composable
fun managerSurfaceColor(): Color = managerAnimatedColor(color = MaterialTheme.colors.surface)

@Composable
fun managerCardColor(): Color = managerAnimatedColor(color = MaterialTheme.colors.cardColor)

@Composable
fun animatedManagerAccentColor(): Color = managerAnimatedColor(color = managerAccentColor())

@Composable
fun managerAnimatedColor(
    color: Color
): Color {
    return animateColorAsState(
        targetValue = color,
        animationSpec = tween(500)
    ).value
}