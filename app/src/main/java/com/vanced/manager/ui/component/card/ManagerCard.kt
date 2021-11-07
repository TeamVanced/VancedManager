package com.vanced.manager.ui.component.card

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.vanced.manager.ui.theme.MediumShape

@Composable
fun ManagerCard(
    modifier: Modifier = Modifier,
    shape: Shape = MediumShape,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    tonalElevation: Dp = 0.dp,
    shadowElevation: Dp = 0.dp,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier,
        shape = shape,
        color = backgroundColor,
        shadowElevation = shadowElevation,
        tonalElevation = tonalElevation,
        content = content
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ManagerCard(
    modifier: Modifier = Modifier,
    shape: Shape = MediumShape,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    tonalElevation: Dp = 0.dp,
    shadowElevation: Dp = 0.dp,
    onClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier,
        shape = shape,
        color = backgroundColor,
        shadowElevation = shadowElevation,
        tonalElevation = tonalElevation,
        onClick = onClick,
        content = content
    )
}