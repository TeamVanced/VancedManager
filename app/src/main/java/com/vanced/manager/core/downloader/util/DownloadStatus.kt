package com.vanced.manager.core.downloader.util

sealed class DownloadStatus {

    object StartInstall : DownloadStatus()

    data class File(val fileName: String) : DownloadStatus()

    data class Progress(val progress: Float) : DownloadStatus()

    data class Error(
        val displayError: String,
        val stacktrace: String,
    ) : DownloadStatus()

}
