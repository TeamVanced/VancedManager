package com.vanced.manager.downloader.impl

import android.content.Context
import com.vanced.manager.downloader.api.MicrogAPI
import com.vanced.manager.downloader.base.AppDownloader
import com.vanced.manager.downloader.util.getMicrogPath
import java.io.File

class MicrogDownloader(
    private val microgAPI: MicrogAPI,
    private val context: Context,
) : AppDownloader() {

    override suspend fun download(
        appVersions: List<String>?,
        onProgress: (Float) -> Unit,
        onFile: (String) -> Unit
    ): DownloadStatus {
        val downloadStatus = downloadFiles(
            files = arrayOf(
                DownloadFile(
                    call = microgAPI.getFile(),
                    fileName = "microg.apk"
                )
            ),
            onProgress = onProgress,
            onFile = onFile
        )
        if (downloadStatus.isError)
            return downloadStatus

        return DownloadStatus.Success
    }

    override suspend fun downloadRoot(
        appVersions: List<String>?,
        onProgress: (Float) -> Unit,
        onFile: (String) -> Unit
    ): DownloadStatus {
        throw IllegalAccessException("Vanced microG does not have a root downloader")
    }

    override fun getSavedFilePath(): String {
        val directory = File(getMicrogPath(context))

        if (!directory.exists())
            directory.mkdirs()

        return directory.path
    }

}