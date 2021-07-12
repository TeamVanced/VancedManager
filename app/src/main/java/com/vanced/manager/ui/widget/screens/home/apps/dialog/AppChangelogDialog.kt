package com.vanced.manager.ui.widget.screens.home.apps.dialog

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vanced.manager.R
import com.vanced.manager.ui.component.dialog.ManagerDialog
import com.vanced.manager.ui.component.text.ManagerText
import com.vanced.manager.ui.resources.managerString
import com.vanced.manager.ui.widget.button.ManagerCloseButton

@Composable
fun AppChangelogDialog(
    appName: String,
    changelog: String,
    onDismissRequest: () -> Unit,
) {
    ManagerDialog(
        title = managerString(R.string.app_info_title, appName),
        onDismissRequest = onDismissRequest,
        buttons = {
            ManagerCloseButton(onClick = onDismissRequest)
        }
    ) {
        ManagerText(
            modifier = Modifier.padding(top = 4.dp),
            text = changelog,
            textStyle = MaterialTheme.typography.subtitle1
        )
    }
}