package com.vanced.manager.ui.component.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.vanced.manager.ui.component.card.ManagerCard
import com.vanced.manager.ui.component.text.ManagerText
import com.vanced.manager.ui.util.defaultContentPaddingHorizontal

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
                modifier = Modifier
                    .fillMaxWidth(),
                textStyle = MaterialTheme.typography.h2,
                textAlign = TextAlign.Center,
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
