package com.vanced.manager.ui.component.card

import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Composable
fun ManagerCard(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium,
    backgroundColor: Color = MaterialTheme.colors.surface,
    content: @Composable () -> Unit,
) {
    Card(
        modifier = modifier,
        shape = shape,
        backgroundColor = backgroundColor,
        elevation = 0.dp,
        content = content
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ManagerCard(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium,
    backgroundColor: Color = MaterialTheme.colors.surface,
    onClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    Card(
        modifier = modifier,
        shape = shape,
        backgroundColor = backgroundColor,
        elevation = 0.dp,
        content = content,
        onClick = onClick
    )
}