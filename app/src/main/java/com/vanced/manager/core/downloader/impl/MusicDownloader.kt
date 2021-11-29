package com.vanced.manager.core.downloader.impl

import android.content.Context
import com.vanced.manager.core.downloader.api.MusicAPI
import com.vanced.manager.core.downloader.base.AppDownloader
import com.vanced.manager.core.downloader.util.DownloadStatus
import com.vanced.manager.core.downloader.util.getVancedMusicPath
import com.vanced.manager.core.preferences.holder.managerVariantPref
import com.vanced.manager.core.preferences.holder.musicVersionPref
import com.vanced.manager.core.util.getLatestOrProvidedAppVersion
import java.io.File

class MusicDownloader(
    private val musicAPI: MusicAPI,
    private val context: Context,
) : AppDownloader() {

    private lateinit var absoluteVersion: String

    override suspend fun download(
        appVersions: List<String>?,
        onStatus: (DownloadStatus) -> Unit
    ) {
        absoluteVersion = getLatestOrProvidedAppVersion(musicVersionPref, appVersions)

        downloadFiles(
            downloadFiles = arrayOf(
                DownloadFile(
                    call = musicAPI.getFiles(
                        version = absoluteVersion,
                        variant = managerVariantPref,
                    ),
                    fileName = "music.apk"
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
        val directory = File(getVancedMusicPath(absoluteVersion, managerVariantPref, context))

        if (!directory.exists())
            directory.mkdirs()

        return directory.path
    }

}