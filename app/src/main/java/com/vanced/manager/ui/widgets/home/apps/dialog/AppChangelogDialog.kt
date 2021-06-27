package com.vanced.manager.ui.widgets.home.apps.dialog

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vanced.manager.R
import com.vanced.manager.ui.components.dialog.ManagerDialog
import com.vanced.manager.ui.resources.managerString
import com.vanced.manager.ui.components.text.ManagerText
import com.vanced.manager.ui.widgets.button.ManagerCloseButton

@Composable
fun AppChangelogDialog(
    appName: String,
    changelog: String,
    showDialog: MutableState<Boolean>
) {
    if (showDialog.value) {
        ManagerDialog(
            title = managerString(R.string.app_info_title, appName),
            onDismissRequest = { showDialog.value = false },
            buttons = {
                ManagerCloseButton {
                    showDialog.value = false
                }
            }
        ) {
            ManagerText(
                modifier = Modifier.padding(top = 4.dp),
                text = changelog,
                textStyle = MaterialTheme.typography.subtitle1
            )
        }
    }
}