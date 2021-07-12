package com.vanced.manager.downloader.impl

import com.vanced.manager.domain.model.App
import com.vanced.manager.downloader.api.VancedAPI
import com.vanced.manager.downloader.base.AppDownloader
import com.vanced.manager.installer.VancedInstaller
import com.vanced.manager.preferences.holder.managerVariantPref
import com.vanced.manager.preferences.holder.vancedLanguagesPref
import com.vanced.manager.preferences.holder.vancedThemePref
import com.vanced.manager.preferences.holder.vancedVersionPref
import com.vanced.manager.ui.viewmodel.HomeViewModel
import com.vanced.manager.util.arch
import com.vanced.manager.util.log

class VancedDownloader(
    private val vancedAPI: VancedAPI,
    vancedInstaller: VancedInstaller
) : AppDownloader(
    appName = "vanced",
    appInstaller = vancedInstaller
) {

    private val theme by vancedThemePref
    private val version by vancedVersionPref
    private val variant by managerVariantPref
    private val languages by vancedLanguagesPref

    override suspend fun download(app: App, viewModel: HomeViewModel) {
        val files = listOf(
            getFile(
                type = "Theme",
                apkName = "$theme.apk"
            ),
            getFile(
                type = "Arch",
                apkName = "split_config.$arch.apk"
            )
        ) + languages.map { language ->
            getFile("Language", "split_config.$language.apk")
        }
        downloadFiles(
            files = files,
            viewModel,
            folderStructure = "$version/$variant",
            onError = {
                log("error", it)
            }
        )
    }

    private fun getFile(
        type: String,
        apkName: String,
    ) = File(
        call = vancedAPI.getApk(
            version = version,
            variant = variant,
            type = type,
            apkName = apkName
        ),
        fileName = apkName
    )

//    private suspend fun downloadTheme() {
//        val theme by vancedThemePref
//        downloadVancedApk(
//            type = "Theme",
//            apkName = "$theme.apk"
//        ) {
//            downloadArch()
//        }
//    }
//
//    private suspend fun downloadArch() {
//        downloadVancedApk(
//            type = "Arch",
//            apkName = "split_config.x86.apk"
//        ) {
//            downloadLanguage()
//        }
//    }
//
//    private suspend fun downloadLanguage() {
//        val languages by vancedLanguagesPref
//        languages.forEach { language ->
//            downloadVancedApk(
//                type = "Language",
//                apkName = "split_config.$language.apk"
//            ) {}
//        }
//        install()
//    }
//
//
//
//
//    private suspend fun downloadVancedApk(
//        type: String,
//        apkName: String,
//        onDownload: suspend () -> Unit,
//    ) {
//        downloadFile(
//            vancedAPI.getApk(
//                version = version,
//                variant = variant,
//                type = type,
//                apkName = apkName
//            ),
//            folderStructure = "$version/$variant",
//            fileName = apkName,
//            onError = {
//                log("error", it)
//            }
//        ) {
//            onDownload()
//        }
//    }

}