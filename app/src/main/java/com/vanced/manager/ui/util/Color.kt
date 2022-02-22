package com.vanced.manager.ui.util

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Color.animated
    @Composable
    get() = animateColorAsState(this, animationSpec = tween(400)).value