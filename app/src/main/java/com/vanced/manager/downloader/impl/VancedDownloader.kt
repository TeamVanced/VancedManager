package com.vanced.manager.downloader.impl

import android.content.Context
import com.vanced.manager.downloader.api.VancedAPI
import com.vanced.manager.downloader.base.AppDownloader
import com.vanced.manager.downloader.util.getVancedYoutubePath
import com.vanced.manager.preferences.holder.managerVariantPref
import com.vanced.manager.preferences.holder.vancedLanguagesPref
import com.vanced.manager.preferences.holder.vancedThemePref
import com.vanced.manager.preferences.holder.vancedVersionPref
import com.vanced.manager.util.arch
import com.vanced.manager.util.getLatestOrProvidedAppVersion
import java.io.File

class VancedDownloader(
    private val vancedAPI: VancedAPI,
    private val context: Context,
) : AppDownloader() {

    private lateinit var absoluteVersion: String

    override suspend fun download(
        appVersions: List<String>?,
        onProgress: (Float) -> Unit,
        onFile: (String) -> Unit
    ): DownloadStatus {
        absoluteVersion = getLatestOrProvidedAppVersion(vancedVersionPref, appVersions)

        val files = arrayOf(
            getFile(
                type = "Theme",
                apkName = "$vancedThemePref.apk",
            ),
            getFile(
                type = "Arch",
                apkName = "split_config.$arch.apk",
            )
        ) + vancedLanguagesPref.map { language ->
            getFile(
                type = "Language",
                apkName = "split_config.$language.apk",
            )
        }

        val downloadStatus = downloadFiles(
            files = files,
            onProgress = onProgress,
            onFile = onFile,
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
        val directory = File(getVancedYoutubePath(absoluteVersion, managerVariantPref, context))

        if (!directory.exists())
            directory.mkdirs()

        return directory.path
    }

    private fun getFile(
        type: String,
        apkName: String,
    ) = DownloadFile(
        call = vancedAPI.getFiles(
            version = absoluteVersion,
            variant = managerVariantPref,
            type = type,
            apkName = apkName
        ),
        fileName = apkName
    )

}