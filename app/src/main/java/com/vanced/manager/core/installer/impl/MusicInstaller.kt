package com.vanced.manager.core.installer.impl

import android.content.Context
import com.vanced.manager.core.downloader.util.getStockYoutubeMusicPath
import com.vanced.manager.core.downloader.util.getVancedYoutubeMusicPath
import com.vanced.manager.core.installer.base.AppInstaller
import com.vanced.manager.core.installer.util.*
import com.vanced.manager.core.preferences.holder.managerVariantPref
import com.vanced.manager.core.preferences.holder.musicVersionPref
import com.vanced.manager.core.preferences.holder.vancedVersionPref
import com.vanced.manager.core.util.VANCED_MUSIC_PACKAGE_ROOT
import com.vanced.manager.core.util.getLatestOrProvidedAppVersion
import java.io.File

class MusicInstaller(
    private val context: Context
) : AppInstaller() {

    override fun install(
        appVersions: List<String>?
    ) {
        val absoluteVersion = getLatestOrProvidedAppVersion(musicVersionPref, appVersions)

        val musicApk = File(getVancedYoutubeMusicPath(absoluteVersion, managerVariantPref, context) + "/music.apk")

        PM.installApp(musicApk, context)
    }

    override fun installRoot(appVersions: List<String>?): PMRootResult<Nothing> {
        val absoluteVersion = getLatestOrProvidedAppVersion(vancedVersionPref, appVersions)

        val stockPath = getStockYoutubeMusicPath(absoluteVersion, context) + "/base.apk"
        val vancedPath = getVancedYoutubeMusicPath(absoluteVersion, "root", context) + "/base.apk"

        val prepareStock = RootPatchHelper.prepareStock(
            stockPackage = VANCED_MUSIC_PACKAGE_ROOT,
            stockVersion = absoluteVersion
        ) {
            PMRoot.installApp(stockPath)
        }
        if (prepareStock.isError)
            return prepareStock

        val patchStock = RootPatchHelper.patchStock(
            patchPath = vancedPath,
            stockPackage = VANCED_MUSIC_PACKAGE_ROOT,
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