package com.vanced.manager.ui.widgets.button

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.vanced.manager.R
import com.vanced.manager.ui.components.button.ManagerThemedTextButton
import com.vanced.manager.ui.components.color.managerAccentColor
import com.vanced.manager.ui.resources.managerString

@Composable
fun ManagerSaveButton(
    backgroundColor: Color = managerAccentColor(),
    onClick: () -> Unit
) {
    ManagerThemedTextButton(
        modifier = Modifier.fillMaxWidth(),
        text = managerString(
            stringId = R.string.dialog_button_save
        ),
        backgroundColor = backgroundColor,
        onClick = onClick
    )
}