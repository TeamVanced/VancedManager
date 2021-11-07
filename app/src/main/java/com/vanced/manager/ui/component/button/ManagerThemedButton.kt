package com.vanced.manager.ui.component.button

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.dp
import com.vanced.manager.ui.component.color.managerAccentColor
import com.vanced.manager.ui.theme.MediumShape

@Composable
fun ManagerThemedButton(
    modifier: Modifier = Modifier,
    backgroundColor: Color = managerAccentColor(),
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        shape = MediumShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor =
            if (backgroundColor.luminance() > 0.7)
                Color.Black
            else
                Color.White
        ),
        elevation = ButtonDefaults.elevatedButtonElevation(0.dp, 0.dp, 0.dp, 0.dp)
    ) {
        content()
    }
}
