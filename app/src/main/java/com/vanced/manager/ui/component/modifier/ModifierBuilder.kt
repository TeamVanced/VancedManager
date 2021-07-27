package com.vanced.manager.ui.component.modifier

import androidx.compose.ui.Modifier

fun modifierBuilder(
    block: Modifier.() -> Unit
) = Modifier.block()