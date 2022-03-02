package com.vanced.manager.installer.impl

import android.content.Context
import com.vanced.manager.domain.model.AppData
import com.vanced.manager.downloader.util.getStockYoutubeMusicPath
import com.vanced.manager.downloader.util.getVancedYoutubeMusicPath
import com.vanced.manager.installer.base.AppInstaller
import com.vanced.manager.installer.util.PM
import com.vanced.manager.installer.util.PMRoot
import com.vanced.manager.installer.util.PMRootResult
import com.vanced.manager.installer.util.RootPatchHelper
import com.vanced.manager.preferences.holder.managerVariantPref
import com.vanced.manager.preferences.holder.musicVersionPref
import com.vanced.manager.preferences.holder.vancedVersionPref
import com.vanced.manager.util.getLatestOrProvidedAppVersion
import java.io.File

class MusicInstaller(
    private val context: Context
) : AppInstaller() {

    override fun install(
        appVersions: List<String>?
    ) {
        val absoluteVersion = getLatestOrProvidedAppVersion(musicVersionPref, appVersions)

        val musicApk = File(
            getVancedYoutubeMusicPath(
                absoluteVersion,
                managerVariantPref,
                context
            ) + "/music.apk"
        )

        PM.installApp(musicApk, context)
    }

    override fun installRoot(appVersions: List<String>?): PMRootResult<Nothing> {
        val absoluteVersion = getLatestOrProvidedAppVersion(vancedVersionPref, appVersions)

        val stockPath = getStockYoutubeMusicPath(absoluteVersion, context) + "/base.apk"
        val vancedPath = getVancedYoutubeMusicPath(absoluteVersion, "root", context) + "/base.apk"

        val prepareStock = RootPatchHelper.prepareStock(
            stockPackage = AppData.PACKAGE_ROOT_VANCED_YOUTUBE_MUSIC,
            stockVersion = absoluteVersion
        ) {
            PMRoot.installApp(stockPath)
        }
        if (prepareStock.isError)
            return prepareStock

        val patchStock = RootPatchHelper.patchStock(
            patchPath = vancedPath,
            stockPackage = AppData.PACKAGE_ROOT_VANCED_YOUTUBE_MUSIC,
            app = APP_KEY
        )
        if (patchStock.isError)
            return patchStock

        return PMRootResult.Success()
    }

    companion object {
        const val APP_KEY = "youtube_music_vanced"
    }

}