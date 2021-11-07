package com.vanced.manager.ui.component.card

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.vanced.manager.ui.theme.MediumShape

@Composable
fun ManagerTonalCard(
    modifier: Modifier = Modifier,
    shape: Shape = MediumShape,
    content: @Composable () -> Unit,
) {
    ManagerCard(
        modifier = modifier,
        shape = shape,
        tonalElevation = 4.dp,
        content = content
    )
}

@Composable
fun ManagerTonalCard(
    modifier: Modifier = Modifier,
    shape: Shape = MediumShape,
    onClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    ManagerCard(
        modifier = modifier,
        shape = shape,
        tonalElevation = 4.dp,
        onClick = onClick,
        content = content
    )
}