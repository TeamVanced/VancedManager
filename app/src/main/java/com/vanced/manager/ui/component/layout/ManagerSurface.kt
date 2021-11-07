package com.vanced.manager.ui.component.layout

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vanced.manager.ui.component.color.managerSurfaceColor

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