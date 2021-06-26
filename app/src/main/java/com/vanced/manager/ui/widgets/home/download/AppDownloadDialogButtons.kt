package com.vanced.manager.ui.widgets.home.download

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import com.vanced.manager.R
import com.vanced.manager.ui.components.button.ManagerThemedTextButton

@Composable
fun AppDownloadDialogButtons(
    showProgress: MutableState<Boolean>,
    onDownloadClick: () -> Unit,
    onCancelClick: () -> Unit,
) {
    when (showProgress.value) {
        true -> ManagerThemedTextButton(
            stringId = R.string.dialog_button_cancel,
            modifier = Modifier.fillMaxWidth(),
            onClick = onCancelClick
        )
        false -> ManagerThemedTextButton(
            stringId = R.string.app_download_dialog_confirm,
            modifier = Modifier.fillMaxWidth(),
            onClick = onDownloadClick
        )
    }
}