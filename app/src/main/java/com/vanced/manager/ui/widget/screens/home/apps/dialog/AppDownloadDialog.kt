package com.vanced.manager.ui.widget.screens.home.apps.dialog

import androidx.compose.runtime.Composable
import com.vanced.manager.downloader.base.AppDownloader
import com.vanced.manager.ui.component.dialog.ManagerDialog
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
        AppDownloadDialogProgress(
            progress = downloader.downloadProgress,
            file = downloader.downloadFile,
            installing = downloader.installing
        )
    }
}