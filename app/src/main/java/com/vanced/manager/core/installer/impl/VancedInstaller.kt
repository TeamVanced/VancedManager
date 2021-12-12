package com.vanced.manager.core.installer.impl

import android.content.Context
import android.content.Intent
import com.vanced.manager.core.downloader.util.getStockYoutubePath
import com.vanced.manager.core.downloader.util.getVancedYoutubePath
import com.vanced.manager.core.installer.base.AppInstaller
import com.vanced.manager.core.installer.util.*
import com.vanced.manager.core.preferences.holder.vancedVersionPref
import com.vanced.manager.core.util.Message
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

    override fun installRoot(appVersions: List<String>?) {

    }

}