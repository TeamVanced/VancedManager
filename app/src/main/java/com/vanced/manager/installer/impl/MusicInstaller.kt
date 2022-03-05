package com.vanced.manager.installer.impl

import android.content.Context
import com.vanced.manager.domain.model.AppData
import com.vanced.manager.downloader.util.getStockYoutubeMusicPath
import com.vanced.manager.downloader.util.getVancedYoutubeMusicPath
import com.vanced.manager.installer.base.AppInstaller
import com.vanced.manager.installer.util.RootPatchHelper
import com.vanced.manager.preferences.holder.managerVariantPref
import com.vanced.manager.preferences.holder.musicVersionPref
import com.vanced.manager.preferences.holder.vancedVersionPref
import com.vanced.manager.repository.manager.NonrootPackageManager
import com.vanced.manager.repository.manager.PackageManagerResult
import com.vanced.manager.repository.manager.RootPackageManager
import com.vanced.manager.util.getLatestOrProvidedAppVersion
import java.io.File

class MusicInstaller(
    private val context: Context,
    private val rootPackageManager: RootPackageManager,
    private val nonrootPackageManager: NonrootPackageManager,
) : AppInstaller() {

    override suspend fun install(appVersions: List<String>?) {
        val absoluteVersion = getLatestOrProvidedAppVersion(musicVersionPref, appVersions)

        val musicApk = File(
            getVancedYoutubeMusicPath(
                absoluteVersion,
                managerVariantPref,
                context
            ) + "/music.apk"
        )

        nonrootPackageManager.installApp(musicApk)
    }

    override suspend fun installRoot(appVersions: List<String>?): PackageManagerResult<Nothing> {
        val absoluteVersion = getLatestOrProvidedAppVersion(vancedVersionPref, appVersions)

        val stock = File(getStockYoutubeMusicPath(absoluteVersion, context), "base.apk")
        val vanced = File(getVancedYoutubeMusicPath(absoluteVersion, "root", context), "base.apk")

        val prepareStock = RootPatchHelper.prepareStock(
            stockPackage = AppData.PACKAGE_ROOT_VANCED_YOUTUBE_MUSIC,
            stockVersion = absoluteVersion
        ) {
            rootPackageManager.installApp(stock)
        }
        if (prepareStock.isError)
            return prepareStock

        val patchStock = RootPatchHelper.patchStock(
            patchPath = vanced.absolutePath,
            stockPackage = AppData.PACKAGE_ROOT_VANCED_YOUTUBE_MUSIC,
            app = APP_KEY
        )
        if (patchStock.isError)
            return patchStock

        return PackageManagerResult.Success(null)
    }

    companion object {
        const val APP_KEY = "youtube_music_vanced"
    }

}