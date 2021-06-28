package com.vanced.manager.ui.widgets.home.apps.dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.vanced.manager.downloader.base.BaseDownloader
import com.vanced.manager.ui.components.dialog.ManagerDialog
import com.vanced.manager.ui.widgets.button.ManagerCancelButton
import com.vanced.manager.ui.widgets.home.download.AppDownloadDialogProgress

@Composable
fun AppDownloadDialog(
    app: String,
    downloader: BaseDownloader,
    onCancelClick: () -> Unit,
) {
    val rememberProgress = remember { downloader.downloadProgress }
    val rememberFile = remember { downloader.downloadFile }
    val rememberInstalling = remember { downloader.installing }

    ManagerDialog(
        title = app,
        onDismissRequest = {},
        buttons = {
            ManagerCancelButton(onClick = onCancelClick)
        }
    ) {
        AppDownloadDialogProgress(
            progress = rememberProgress,
            file = rememberFile,
            installing = rememberInstalling
        )
    }
}