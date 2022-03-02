package com.vanced.manager.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import com.vanced.manager.ui.theme.MediumShape
import com.vanced.manager.ui.util.animated

@Composable
fun ManagerCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    shape: Shape = MediumShape,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(containerColor),
    elevation: CardElevation = CardDefaults.cardElevation(),
    content: @Composable ColumnScope.() -> Unit,
) {
    if (onClick != null) {
        val interactionSource = remember { MutableInteractionSource() }
        Card(
            modifier = modifier
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onClick
                ),
            interactionSource = interactionSource,
            shape = shape,
            containerColor = containerColor.animated,
            contentColor = contentColor.animated,
            elevation = elevation,
            content = content
        )
    } else {
        Card(
            modifier = modifier,
            shape = shape,
            containerColor = containerColor.animated,
            contentColor = contentColor.animated,
            elevation = elevation,
            content = content
        )
    }
}

@Composable
fun ManagerElevatedCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    shape: Shape = MediumShape,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(containerColor),
    elevation: CardElevation = CardDefaults.elevatedCardElevation(),
    content: @Composable ColumnScope.() -> Unit,
) {
    if (onClick != null) {
        val interactionSource = remember { MutableInteractionSource() }
        ElevatedCard(
            modifier = modifier
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onClick
                ),
            interactionSource = interactionSource,
            shape = shape,
            containerColor = containerColor.animated,
            contentColor = contentColor.animated,
            elevation = elevation,
            content = content
        )
    } else {
        ElevatedCard(
            modifier = modifier,
            shape = shape,
            containerColor = containerColor.animated,
            contentColor = contentColor.animated,
            elevation = elevation,
            content = content
        )
    }
}

@Composable
fun ManagerOutlinedCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    shape: Shape = MediumShape,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(containerColor),
    elevation: CardElevation = CardDefaults.outlinedCardElevation(),
    content: @Composable ColumnScope.() -> Unit,
) {
    if (onClick != null) {
        val interactionSource = remember { MutableInteractionSource() }
        OutlinedCard(
            modifier = modifier
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onClick
                ),
            interactionSource = interactionSource,
            shape = shape,
            containerColor = containerColor.animated,
            contentColor = contentColor.animated,
            elevation = elevation,
            content = content
        )
    } else {
        OutlinedCard(
            modifier = modifier,
            shape = shape,
            containerColor = containerColor.animated,
            contentColor = contentColor.animated,
            elevation = elevation,
            content = content
        )
    }
}