package com.vanced.manager.installer.impl

import android.content.Context
import com.vanced.manager.domain.model.AppData
import com.vanced.manager.downloader.util.getStockYoutubePath
import com.vanced.manager.downloader.util.getVancedYoutubePath
import com.vanced.manager.installer.base.AppInstaller
import com.vanced.manager.installer.util.RootPatchHelper
import com.vanced.manager.preferences.holder.vancedVersionPref
import com.vanced.manager.repository.manager.NonrootPackageManager
import com.vanced.manager.repository.manager.PackageManagerResult
import com.vanced.manager.repository.manager.RootPackageManager
import com.vanced.manager.util.getLatestOrProvidedAppVersion
import java.io.File

class VancedInstaller(
    private val context: Context,
    private val rootPackageManager: RootPackageManager,
    private val nonrootPackageManager: NonrootPackageManager,
) : AppInstaller() {

    override suspend fun install(appVersions: List<String>?) {
        val absoluteVersion = getLatestOrProvidedAppVersion(vancedVersionPref, appVersions)

        val apks = File(getVancedYoutubePath(absoluteVersion, "nonroot", context))
            .listFiles()

        nonrootPackageManager.installSplitApp(apks!!)
    }

    override suspend fun installRoot(appVersions: List<String>?): PackageManagerResult<Nothing> {
        val absoluteVersion = getLatestOrProvidedAppVersion(vancedVersionPref, appVersions)

        val stockApks = File(getStockYoutubePath(absoluteVersion, context))
            .listFiles()
        val vancedBaseApk = getVancedYoutubePath(absoluteVersion, "root", context) + "/base.apk"

        val prepareStock = RootPatchHelper.prepareStock(
            stockPackage = AppData.PACKAGE_ROOT_VANCED_YOUTUBE,
            stockVersion = absoluteVersion,
        ) {
            rootPackageManager.installSplitApp(stockApks!!)
        }
        if (prepareStock.isError)
            return prepareStock

        val patchStock = RootPatchHelper.patchStock(
            patchPath = vancedBaseApk,
            stockPackage = AppData.PACKAGE_ROOT_VANCED_YOUTUBE,
            app = APP_KEY
        )
        if (patchStock.isError)
            return patchStock

        return PackageManagerResult.Success(null)
    }

    companion object {
        const val APP_KEY = "youtube_vanced"
    }
}