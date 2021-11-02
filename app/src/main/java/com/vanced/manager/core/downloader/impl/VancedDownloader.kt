package com.vanced.manager.core.downloader.impl

import android.content.Context
import com.vanced.manager.core.downloader.api.VancedAPI
import com.vanced.manager.core.downloader.base.AppDownloader
import com.vanced.manager.core.downloader.util.DownloadStatus
import com.vanced.manager.core.preferences.holder.managerVariantPref
import com.vanced.manager.core.preferences.holder.vancedLanguagesPref
import com.vanced.manager.core.preferences.holder.vancedThemePref
import com.vanced.manager.core.preferences.holder.vancedVersionPref
import com.vanced.manager.core.util.arch
import com.vanced.manager.core.util.getLatestOrProvidedAppVersion
import java.io.File

class VancedDownloader(
    private val vancedAPI: VancedAPI,
    private val context: Context,
) : AppDownloader() {

    private val theme by vancedThemePref
    private val version by vancedVersionPref
    private val variant by managerVariantPref
    private val languages by vancedLanguagesPref

    private lateinit var absoluteVersion: String

    override suspend fun download(
        appVersions: List<String>?,
        onStatus: (DownloadStatus) -> Unit
    ) {
        absoluteVersion = getLatestOrProvidedAppVersion(version, appVersions)

        val files = arrayOf(
            getFile(
                type = "Theme",
                apkName = "$theme.apk",
            ),
            getFile(
                type = "Arch",
                apkName = "split_config.$arch.apk",
            )
        ) + languages.map { language ->
            getFile(
                type = "Language",
                apkName = "split_config.$language.apk",
            )
        }

        downloadFiles(
            downloadFiles = files,
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
                onStatus(DownloadStatus.Error(
                    displayError = "Failed to download $fileName",
                    stacktrace = error
                ))
            }
        )
    }

    override fun getSavedFilePath(): String {
        val directory =
            File(context.getExternalFilesDir("vanced")!!.path + "/$absoluteVersion/$variant")

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
            variant = variant,
            type = type,
            apkName = apkName
        ),
        fileName = apkName
    )

}