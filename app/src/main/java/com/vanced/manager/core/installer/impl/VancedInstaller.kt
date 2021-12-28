package com.vanced.manager.core.installer.impl

import android.content.Context
import com.vanced.manager.core.downloader.util.getStockYoutubePath
import com.vanced.manager.core.downloader.util.getVancedYoutubePath
import com.vanced.manager.core.installer.base.AppInstaller
import com.vanced.manager.core.installer.util.*
import com.vanced.manager.core.preferences.holder.vancedVersionPref
import com.vanced.manager.core.util.VANCED_YOUTUBE_PACKAGE_ROOT
import com.vanced.manager.core.util.getLatestOrProvidedAppVersion
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
            stockPackage = VANCED_YOUTUBE_PACKAGE_ROOT,
            stockVersion = absoluteVersion,
        ) {
            PMRoot.installSplitApp(stockApks!!)
        }
        if (prepareStock.isError)
            return prepareStock

        val patchStock = RootPatchHelper.patchStock(
            patchPath = vancedBaseApk,
            stockPackage = VANCED_YOUTUBE_PACKAGE_ROOT,
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