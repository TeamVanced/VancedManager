package com.vanced.manager.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Composable
fun ManagerButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    elevation: ButtonElevation? = ButtonDefaults.buttonElevation(),
    shape: Shape = RoundedCornerShape(20.0.dp),
    border: BorderStroke? = null,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        onClick,
        modifier,
        enabled,
        interactionSource,
        elevation,
        shape,
        border,
        colors,
        contentPadding,
        content
    )
}

@Composable
fun ManagerElevatedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    elevation: ButtonElevation? = ButtonDefaults.elevatedButtonElevation(),
    shape: Shape = RoundedCornerShape(20.0.dp),
    border: BorderStroke? = null,
    colors: ButtonColors = ButtonDefaults.elevatedButtonColors(),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit
) {
    ElevatedButton(
        onClick,
        modifier,
        enabled,
        interactionSource,
        elevation,
        shape,
        border,
        colors,
        contentPadding,
        content
    )
}

@Composable
fun ManagerFilledTonalButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    elevation: ButtonElevation? = ButtonDefaults.filledTonalButtonElevation(),
    shape: Shape = RoundedCornerShape(20.0.dp),
    border: BorderStroke? = null,
    colors: ButtonColors = ButtonDefaults.filledTonalButtonColors(),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit
) {
    FilledTonalButton(
        onClick,
        modifier,
        enabled,
        interactionSource,
        elevation,
        shape,
        border,
        colors,
        contentPadding,
        content
    )
}

@Composable
fun ManagerOutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    elevation: ButtonElevation? = null,
    shape: Shape = RoundedCornerShape(20.0.dp),
    border: BorderStroke? = ButtonDefaults.outlinedButtonBorder,
    colors: ButtonColors = ButtonDefaults.outlinedButtonColors(),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        onClick,
        modifier,
        enabled,
        interactionSource,
        elevation,
        shape,
        border,
        colors,
        contentPadding,
        content
    )
}

@Composable
fun ManagerTextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    elevation: ButtonElevation? = null,
    shape: Shape = RoundedCornerShape(20.0.dp),
    border: BorderStroke? = null,
    colors: ButtonColors = ButtonDefaults.textButtonColors(),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit
) {
    TextButton(
        onClick,
        modifier,
        enabled,
        interactionSource,
        elevation,
        shape,
        border,
        colors,
        contentPadding,
        content
    )
}