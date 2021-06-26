package com.vanced.manager.downloader.impl

import com.vanced.manager.downloader.api.VancedAPI
import com.vanced.manager.downloader.base.BaseDownloader

class VancedDownloader(
    private val vancedAPI: VancedAPI,
) : BaseDownloader(
    appName = "vanced"
) {

    private lateinit var version: String
    private lateinit var variant: String

    private lateinit var folderStructure: String

    override suspend fun download() {
        version = "v16.16.38"
        variant = "nonroot"
        folderStructure = "$appName/$version/$variant"
        downloadTheme()
    }

    private suspend fun downloadTheme() {
        downloadVancedApk(
            type = "Theme",
            apkName = "black.apk"
        ) {
            downloadArch()
        }
    }

    private suspend fun downloadArch() {
        downloadVancedApk(
            type = "Arch",
            apkName = "split_config.x86.apk"
        ) {
            downloadLanguage()
        }
    }

    private suspend fun downloadLanguage() {
        downloadVancedApk(
            type = "Language",
            apkName = "split_config.en.apk"
        ) {
            appInstaller.installVanced(version)
        }
    }

    private suspend fun downloadVancedApk(
        type: String,
        apkName: String,
        onDownload: suspend () -> Unit,
    ) {
        downloadFile(
            vancedAPI.getApk(
                version = version,
                variant = variant,
                type = type,
                apkName = apkName
            ),
            folderStructure = folderStructure,
            fileName = apkName,
            onError = {

            }
        ) {
            onDownload()
        }
    }

}