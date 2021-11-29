package com.vanced.manager.core.installer.impl

import android.content.Context
import com.vanced.manager.core.downloader.util.getMicrogPath
import com.vanced.manager.core.installer.base.AppInstaller
import com.vanced.manager.core.installer.util.installApp
import java.io.File

class MicrogInstaller(
    private val context: Context
) : AppInstaller() {

    override fun install(
        appVersions: List<String>?
    ) {
        val musicApk = File(getMicrogPath(context) + "/microg.apk")

        installApp(musicApk, context)
    }

}