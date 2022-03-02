package com.vanced.manager.downloader.impl

import android.content.Context
import com.vanced.manager.downloader.api.MusicAPI
import com.vanced.manager.downloader.base.AppDownloader
import com.vanced.manager.downloader.util.getVancedYoutubeMusicPath
import com.vanced.manager.preferences.holder.managerVariantPref
import com.vanced.manager.preferences.holder.musicVersionPref
import com.vanced.manager.util.getLatestOrProvidedAppVersion
import java.io.File

class MusicDownloader(
    private val musicAPI: MusicAPI,
    private val context: Context,
) : AppDownloader() {

    private lateinit var absoluteVersion: String

    override suspend fun download(
        appVersions: List<String>?,
        onProgress: (Float) -> Unit,
        onFile: (String) -> Unit
    ): DownloadStatus {
        absoluteVersion = getLatestOrProvidedAppVersion(musicVersionPref, appVersions)

        val downloadStatus = downloadFiles(
            files = arrayOf(
                DownloadFile(
                    call = musicAPI.getFiles(
                        version = absoluteVersion,
                        variant = managerVariantPref,
                    ),
                    fileName = "music.apk"
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
        return DownloadStatus.Success
    }

    override fun getSavedFilePath(): String {
        val directory =
            File(getVancedYoutubeMusicPath(absoluteVersion, managerVariantPref, context))

        if (!directory.exists())
            directory.mkdirs()

        return directory.path
    }

}