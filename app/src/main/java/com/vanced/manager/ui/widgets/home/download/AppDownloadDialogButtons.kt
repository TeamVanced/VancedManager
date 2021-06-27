package com.vanced.manager.ui.widgets.home.download

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.vanced.manager.ui.widgets.button.ManagerCancelButton
import com.vanced.manager.ui.widgets.button.ManagerDownloadButton

@Composable
fun AppDownloadDialogButtons(
    showProgress: MutableState<Boolean>,
    onDownloadClick: () -> Unit,
    onCancelClick: () -> Unit,
) {
    when (showProgress.value) {
        true -> ManagerCancelButton(onClick = onCancelClick)
        false -> ManagerDownloadButton(onClick = onDownloadClick)
    }
}