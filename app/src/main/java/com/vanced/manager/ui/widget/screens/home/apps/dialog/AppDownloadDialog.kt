package com.vanced.manager.ui.widget.screens.home.apps.dialog

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.vanced.manager.R
import com.vanced.manager.downloader.base.AppDownloader
import com.vanced.manager.ui.component.dialog.ManagerDialog
import com.vanced.manager.ui.component.text.ManagerText
import com.vanced.manager.ui.widget.button.ManagerCancelButton
import com.vanced.manager.ui.widget.screens.home.download.AppDownloadDialogProgress

@Composable
fun AppDownloadDialog(
    app: String,
    downloader: AppDownloader,
    onCancelClick: () -> Unit,
) {
    ManagerDialog(
        title = app,
        onDismissRequest = {},
        buttons = {
            ManagerCancelButton(onClick = onCancelClick)
        }
    ) {
        ManagerText(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.app_download_dialog_subtitle),
            textStyle = MaterialTheme.typography.subtitle2,
            textAlign = TextAlign.Center
        )
        AppDownloadDialogProgress(
            progress = downloader.downloadProgress,
            file = downloader.downloadFile,
            installing = downloader.installing
        )
    }
}