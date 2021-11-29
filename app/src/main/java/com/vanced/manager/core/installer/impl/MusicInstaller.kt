package com.vanced.manager.core.installer.impl

import android.content.Context
import com.vanced.manager.core.downloader.util.getVancedMusicPath
import com.vanced.manager.core.installer.base.AppInstaller
import com.vanced.manager.core.installer.util.installApp
import com.vanced.manager.core.preferences.holder.managerVariantPref
import com.vanced.manager.core.preferences.holder.musicVersionPref
import com.vanced.manager.core.preferences.holder.vancedVersionPref
import com.vanced.manager.core.util.getLatestOrProvidedAppVersion
import java.io.File

class MusicInstaller(
    private val context: Context
) : AppInstaller() {

    override fun install(
        appVersions: List<String>?
    ) {
        val absoluteVersion = getLatestOrProvidedAppVersion(musicVersionPref, appVersions)

        val musicApk = File(getVancedMusicPath(absoluteVersion, managerVariantPref, context) + "/music.apk")

        installApp(musicApk, context)
    }

}