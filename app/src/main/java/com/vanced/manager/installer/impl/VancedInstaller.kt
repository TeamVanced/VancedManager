package com.vanced.manager.installer.impl

import android.content.Context
import com.vanced.manager.domain.model.AppData
import com.vanced.manager.downloader.util.getStockYoutubePath
import com.vanced.manager.downloader.util.getVancedYoutubePath
import com.vanced.manager.installer.base.AppInstaller
import com.vanced.manager.installer.util.PM
import com.vanced.manager.installer.util.PMRoot
import com.vanced.manager.installer.util.PMRootResult
import com.vanced.manager.installer.util.RootPatchHelper
import com.vanced.manager.preferences.holder.vancedVersionPref
import com.vanced.manager.util.getLatestOrProvidedAppVersion
import java.io.File

class VancedInstaller(
    private val context: Context
) : AppInstaller() {

    override fun install(
        appVersions: List<String>?
    ) {
        val absoluteVersion = getLatestOrProvidedAppVersion(vancedVersionPref, appVersions)

        val apks = File(getVancedYoutubePath(absoluteVersion, "nonroot", context))
            .listFiles()

        PM.installSplitApp(apks!!, context)
    }

    override fun installRoot(appVersions: List<String>?): PMRootResult<Nothing> {
        val absoluteVersion = getLatestOrProvidedAppVersion(vancedVersionPref, appVersions)

        val stockApks = File(getStockYoutubePath(absoluteVersion, context))
            .listFiles()?.map { it.absolutePath }
        val vancedBaseApk = getVancedYoutubePath(absoluteVersion, "root", context) + "/base.apk"

        val prepareStock = RootPatchHelper.prepareStock(
            stockPackage = AppData.PACKAGE_ROOT_VANCED_YOUTUBE,
            stockVersion = absoluteVersion,
        ) {
            PMRoot.installSplitApp(stockApks!!)
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

        return PMRootResult.Success()
    }

    companion object {
        const val APP_KEY = "youtube_vanced"
    }
}