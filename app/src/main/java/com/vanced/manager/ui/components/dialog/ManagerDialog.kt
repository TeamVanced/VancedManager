package com.vanced.manager.ui.components.dialog

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.vanced.manager.ui.components.card.ManagerCard
import com.vanced.manager.ui.components.text.ManagerText
import com.vanced.manager.ui.utils.defaultContentPaddingHorizontal

@Composable
fun ManagerDialog(
    @StringRes titleId: Int,
    onDismissRequest: () -> Unit,
    buttons: @Composable ColumnScope.() -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    ManagerDialog(
        title = {
            ManagerText(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                stringId = titleId,
                textStyle = MaterialTheme.typography.h2
            )
        },
        onDismissRequest = onDismissRequest,
        buttons = buttons,
        content = content
    )
}

@Composable
fun ManagerDialog(
    title: String,
    onDismissRequest: () -> Unit,
    buttons: @Composable ColumnScope.() -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    ManagerDialog(
        title = {
            ManagerText(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                textStyle = MaterialTheme.typography.h2,
                text = title
            )
        },
        onDismissRequest = onDismissRequest,
        buttons = buttons,
        content = content
    )
}

@Composable
fun ManagerDialog(
    title: @Composable ColumnScope.() -> Unit,
    onDismissRequest: () -> Unit,
    buttons: @Composable ColumnScope.() -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        content = {
            ManagerCard {
                Column(
                    modifier = Modifier.padding(defaultContentPaddingHorizontal),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    title()
                    content()
                    buttons()
                }
            }
        }
    )
}
