package com.vanced.manager.installer.impl

import android.content.Context
import com.vanced.manager.downloader.util.getMicrogPath
import com.vanced.manager.installer.base.AppInstaller
import com.vanced.manager.installer.util.PM
import com.vanced.manager.installer.util.PMRootResult
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

    override fun installRoot(appVersions: List<String>?): PMRootResult<Nothing> {
        throw IllegalAccessException("Vanced microG does not have a root installer")
    }

}