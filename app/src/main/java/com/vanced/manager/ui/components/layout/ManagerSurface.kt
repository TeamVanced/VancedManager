package com.vanced.manager.ui.components.layout

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vanced.manager.ui.components.color.managerSurfaceColor

@Composable
fun ManagerSurface(
    content: @Composable () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = managerSurfaceColor(),
        content = content
    )
}