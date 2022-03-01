package com.vanced.manager.datasource

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build

interface PkgInfoDatasource {

    fun getVersionCode(packageName: String): Int?

    fun getVersionName(packageName: String): String?

}

class PkgInfoDatasourceImpl(
    private val packageManager: PackageManager
) : PkgInfoDatasource {

    private companion object {
        const val FLAG_NOTHING = 0
        const val VERSION_IGNORE_MAJOR = 0xFFFFFFFF
    }

    @SuppressLint("WrongConstant")
    override fun getVersionCode(packageName: String): Int? {
        return try {
            val packageInfo = packageManager.getPackageInfo(packageName, FLAG_NOTHING)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageInfo.longVersionCode.and(VERSION_IGNORE_MAJOR).toInt()
            } else {
                @Suppress("DEPRECATION")
                packageInfo.versionCode
            }
        } catch (e: PackageManager.NameNotFoundException) {
            null
        }
    }

    @Suppress("DEPRECATION")
    @SuppressLint("WrongConstant")
    override fun getVersionName(packageName: String): String? {
        return try {
           packageManager
                .getPackageInfo(packageName, FLAG_NOTHING)
                .versionName
        } catch (e: PackageManager.NameNotFoundException) {
            null
        }
    }
}