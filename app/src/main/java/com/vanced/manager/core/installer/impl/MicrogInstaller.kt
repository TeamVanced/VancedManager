package com.vanced.manager.core.installer.impl

import android.content.Context
import com.vanced.manager.core.downloader.util.getMicrogPath
import com.vanced.manager.core.installer.base.AppInstaller
import com.vanced.manager.core.installer.util.PM
import java.io.File

class MicrogInstaller(
    private val context: Context
) : AppInstaller() {

    override fun install(
        appVersions: List<String>?
    ) {
        val musicApk = File(getMicrogPath(context) + "/microg.apk")

        PM.installApp(musicApk, context)
    }

    override fun installRoot(appVersions: List<String>?) {
        throw IllegalAccessException("Vanced microG does not have a root installer")
    }

}