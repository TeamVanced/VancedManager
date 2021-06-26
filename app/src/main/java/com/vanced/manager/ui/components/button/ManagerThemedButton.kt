package com.vanced.manager.ui.components.button

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.dp
import com.vanced.manager.ui.components.color.managerAccentColor

@Composable
fun ManagerThemedButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    val accentColor = managerAccentColor()
    Button(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = managerAccentColor()
        ),
        elevation = ButtonDefaults.elevation(0.dp, 0.dp, 0.dp)
    ) {
        CompositionLocalProvider(LocalContentColor provides if (accentColor.luminance() > 0.7) Color.Black else Color.White) {
            content()
        }
    }
}
