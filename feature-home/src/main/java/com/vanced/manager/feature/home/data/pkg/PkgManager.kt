package com.vanced.manager.feature.home.data.pkg

import android.content.pm.PackageManager
import android.os.Build


interface PkgManager {

    @Throws(PackageManager.NameNotFoundException::class)
    suspend fun getVersionCode(packageName: String): Int

    @Throws(PackageManager.NameNotFoundException::class)
    suspend fun getVersionName(packageName: String): String
}

class PkgManagerImpl(
    private val packageManager: PackageManager
) : PkgManager {

    private companion object {
        const val PACKAGE_FLAG_ALL_OFF = 0
        const val MAJOR_IGNORE = 0xFFFFFFFF
    }

    @Suppress("DEPRECATION")
    @Throws(PackageManager.NameNotFoundException::class)
    override suspend fun getVersionCode(packageName: String): Int {
        return with(packageManager) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                getPackageInfo(packageName, PACKAGE_FLAG_ALL_OFF)
                    .longVersionCode
                    .and(MAJOR_IGNORE)
                    .toInt()
            } else {
                getPackageInfo(packageName, PACKAGE_FLAG_ALL_OFF)
                    .versionCode
            }
        }
    }

    @Throws(PackageManager.NameNotFoundException::class)
    override suspend fun getVersionName(packageName: String): String =
        packageManager.getPackageInfo(packageName, PACKAGE_FLAG_ALL_OFF)
            .versionName
}