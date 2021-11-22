package com.vanced.manager.core.downloader.impl

import android.content.Context
import com.vanced.manager.core.downloader.api.MicrogAPI
import com.vanced.manager.core.downloader.base.AppDownloader
import com.vanced.manager.core.downloader.util.DownloadStatus
import java.io.File

class MicrogDownloader(
    private val microgAPI: MicrogAPI,
    private val context: Context,
) : AppDownloader() {

    override suspend fun download(
        appVersions: List<String>?,
        onStatus: (DownloadStatus) -> Unit
    ) {
        downloadFiles(
            downloadFiles = arrayOf(
                DownloadFile(
                    call = microgAPI.getFile(),
                    fileName = "microg.apk"
                )
            ),
            onProgress = { progress ->
                onStatus(DownloadStatus.Progress(progress))
            },
            onFile = { fileName ->
                onStatus(DownloadStatus.File(fileName))
            },
            onSuccess = {
                onStatus(DownloadStatus.StartInstall)
            },
            onError = { error, fileName ->
                onStatus(
                    DownloadStatus.Error(
                        displayError = "Failed to download $fileName",
                        stacktrace = error
                    )
                )
            }
        )
    }

    override fun getSavedFilePath(): String {
        val directory =
            File(context.getExternalFilesDir("microg")!!.path)

        if (!directory.exists())
            directory.mkdirs()

        return directory.path
    }

}