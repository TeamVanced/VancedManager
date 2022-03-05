package com.vanced.manager.installer.impl

import android.content.Context
import com.vanced.manager.downloader.util.getMicrogPath
import com.vanced.manager.installer.base.AppInstaller
import com.vanced.manager.repository.manager.NonrootPackageManager
import com.vanced.manager.repository.manager.PackageManagerResult
import java.io.File

class MicrogInstaller(
    private val context: Context,
    private val nonrootPackageManager: NonrootPackageManager,
) : AppInstaller() {

    override suspend fun install(appVersions: List<String>?) {
        val musicApk = File(getMicrogPath(context), "microg.apk")

        nonrootPackageManager.installApp(musicApk)
    }

    override suspend fun installRoot(appVersions: List<String>?): PackageManagerResult<Nothing> {
        throw IllegalAccessException("Vanced microG does not have a root installer")
    }

}