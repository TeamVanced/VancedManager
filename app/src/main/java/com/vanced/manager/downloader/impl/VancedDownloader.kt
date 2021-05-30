package com.vanced.manager.downloader.impl

import com.vanced.manager.downloader.api.VancedAPI
import com.vanced.manager.downloader.base.BaseDownloader
import com.vanced.manager.ui.composables.InstallationOption
import com.vanced.manager.ui.composables.InstallationOptionKey

class VancedDownloader(
    private val vancedAPI: VancedAPI
) : BaseDownloader(
    appName = "vanced"
) {

    private val themeInstallationOption = InstallationOption(
        title = "Languages",
        descriptionKey = InstallationOptionKey(
            keyName = "vanced_languages",
            keyDefaultValue = "en"
        )
    ) { show, preference ->
//        DialogRadioButtonPreference(
//            preferenceTitle = "Language",
//            preferenceKey = ,
//            defaultValue = ,
//            buttons =
//        )
    }

    private val versionInstallationOption = InstallationOption(
        title = "Languages",
        descriptionKey = InstallationOptionKey(
            keyName = "vanced_languages",
            keyDefaultValue = "en"
        )
    ) { show, preference ->
//        DialogRadioButtonPreference(
//            preferenceTitle = "Language",
//            preferenceKey = ,
//            defaultValue = ,
//            buttons =
//        )
    }

    private val languageInstallationOption = InstallationOption(
        title = "Languages",
        descriptionKey = InstallationOptionKey(
            keyName = "vanced_languages",
            keyDefaultValue = "en"
        )
    ) { show, preference ->
//        DialogRadioButtonPreference(
//            preferenceTitle = "Language",
//            preferenceKey = ,
//            defaultValue = ,
//            buttons =
//        )
    }

    override val installationOptions: List<InstallationOption> get() = listOf(
        languageInstallationOption
    )

    override suspend fun download() {
        downloadTheme()
    }

    private suspend fun downloadTheme() {
        downloadFile(
            vancedAPI.getApk(
                version = "16.16.38",
                variant = "nonroot",
                type = "Theme",
                apkName = "black.apk"
            ),
            folderStructure = "vanced/nonroot/v16.16.38",
            fileName = "black.apk"
        ) {
            downloadArch()
        }
    }

    private suspend fun downloadArch() {
        downloadFile(
            vancedAPI.getApk(
                version = "16.16.38",
                variant = "nonroot",
                type = "Arch",
                apkName = "split_config.x86.apk"
            ),
            folderStructure = "vanced/nonroot/arch",
            fileName = "black.apk"
        ) {
            downloadLanguage()
        }
    }

    private suspend fun downloadLanguage() {
        downloadFile(
            vancedAPI.getApk(
                version = "16.16.38",
                variant = "nonroot",
                type = "Arch",
                apkName = "split_config.x86.apk"
            ),
            folderStructure = "vanced/nonroot/lang",
            fileName = "black.apk"
        ) {
            downloadLanguage()
        }
    }

}