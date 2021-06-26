package com.vanced.manager.ui.widgets.home.apps.dialog

import androidx.compose.runtime.*
import com.vanced.manager.downloader.base.BaseDownloader
import com.vanced.manager.ui.components.dialog.ManagerDialog
import com.vanced.manager.ui.widgets.home.download.AppDownloadDialogButtons
import com.vanced.manager.ui.widgets.home.download.AppDownloadDialogProgress
import kotlinx.coroutines.launch

@Composable
fun AppDownloadDialog(
    app: String,
    downloader: BaseDownloader,
    showDialog: MutableState<Boolean>
) {
    val coroutineScope = rememberCoroutineScope()

    val rememberProgress = remember { downloader.downloadProgress }
    val rememberFile = remember { downloader.downloadFile }
    val rememberInstalling = remember { downloader.installing }

    val showProgress = remember { mutableStateOf(false) }

    if (showDialog.value) {
        ManagerDialog(
            title = app,
            onDismissRequest = { showDialog.value = false },
            buttons = {
                AppDownloadDialogButtons(
                    showProgress = showProgress,
                    onDownloadClick = {
                        coroutineScope.launch {
                            showProgress.value = true
                            downloader.download()
                        }
                    },
                    onCancelClick = {
                        downloader.cancelDownload()
                        showDialog.value = false
                        showProgress.value = false
                    }
                )
            }
        ) {
            AppDownloadDialogProgress(
                progress = rememberProgress,
                file = rememberFile,
                showProgress = showProgress.value,
                installing = rememberInstalling
            )
        }
    }
}