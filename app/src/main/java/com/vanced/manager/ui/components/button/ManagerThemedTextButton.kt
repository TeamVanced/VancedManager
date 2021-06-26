package com.vanced.manager.ui.components.button

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vanced.manager.ui.components.text.ManagerText

@Composable
fun ManagerThemedTextButton(
    modifier: Modifier = Modifier,
    @StringRes stringId: Int,
    onClick: () -> Unit
) {
    ManagerThemedButton(
        modifier = modifier,
        onClick = onClick
    ) {
        ManagerText(stringId = stringId)
    }
}