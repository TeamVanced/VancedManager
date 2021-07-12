package com.vanced.manager.ui.component.card

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import com.vanced.manager.ui.component.color.managerThemedCardColor

@Composable
fun ManagerThemedCard(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium,
    content: @Composable () -> Unit,
) {
    ManagerCard(
        modifier = modifier,
        shape = shape,
        backgroundColor = managerThemedCardColor(),
        content = content
    )
}

@Composable
fun ManagerClickableThemedCard(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium,
    onClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    ManagerCard(
        modifier = modifier,
        shape = shape,
        backgroundColor = managerThemedCardColor(),
        onClick = onClick,
    ) {
        content()
    }
}