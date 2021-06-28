package com.vanced.manager.ui.components.animation

import android.annotation.SuppressLint
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.keyframes
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun <T> Transition<T>.springAnimation(
    initialValue: Dp,
    label: String
) = animateDp(
    transitionSpec = {
        keyframes {
            durationMillis = 250
            initialValue - 8.dp at 50
            initialValue + 8.dp at 150
            initialValue at 250
        }
    },
    label = label
) { initialValue }