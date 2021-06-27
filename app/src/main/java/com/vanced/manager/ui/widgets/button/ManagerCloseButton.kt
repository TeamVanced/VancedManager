package com.vanced.manager.ui.widgets.button

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vanced.manager.R
import com.vanced.manager.ui.components.button.ManagerThemedTextButton
import com.vanced.manager.ui.resources.managerString

@Composable
fun ManagerCloseButton(
    onClick: () -> Unit
) {
    ManagerThemedTextButton(
        modifier = Modifier.fillMaxWidth(),
        text = managerString(
            stringId = R.string.dialog_button_close
        ),
        onClick = onClick
    )
}