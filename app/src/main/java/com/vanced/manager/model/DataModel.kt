package com.vanced.manager.model

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
import com.beust.klaxon.JsonObject
import com.vanced.manager.R
import com.vanced.manager.core.CombinedLiveData
import com.vanced.manager.utils.PackageHelper.isPackageInstalled

open class DataModel(
    jsonObject: LiveData<JsonObject?>,
    context: Context,
    val appPkg: String,
    val appName: String,
    val appDescription: String,
    @DrawableRes val appIcon: Int
) {

    val isAppInstalled = Transformations.map(jsonObject) { isAppInstalled(appPkg) }

    private val versionCode = Transformations.map(jsonObject) { jobj ->
        jobj?.int("versionCode") ?: 0
    }
    private val installedVersionCode = Transformations.map(isAppInstalled) {
        getPkgVersionCode(appPkg, it)
    }
    private val unavailable = context.getString(R.string.unavailable)
    private val pm = context.packageManager

    val versionName = Transformations.map(jsonObject) { jobj ->
        jobj?.string("version") ?: unavailable
    }
    val changelog = Transformations.map(jsonObject) { jobj ->
        jobj?.string("changelog") ?: unavailable
    }
    val installedVersionName = Transformations.map(isAppInstalled) {
        getPkgVersionName(appPkg, it)
    }
    val buttonTag = CombinedLiveData(versionCode, installedVersionCode) { versionCode, installedVersionCode ->
        compareInt(installedVersionCode, versionCode)
    }

    open fun isAppInstalled(pkg: String): Boolean = isPackageInstalled(pkg, pm)

    private fun getPkgVersionName(pkg: String, isAppInstalled: Boolean): String {
        return if (isAppInstalled) {
            pm?.getPackageInfo(pkg, 0)?.versionName?.removeSuffix("-vanced") ?: unavailable
        } else {
            unavailable
        }
    }

    @Suppress("DEPRECATION")
    private fun getPkgVersionCode(pkg: String, isAppInstalled: Boolean): Int {
        return if (isAppInstalled) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                pm?.getPackageInfo(pkg, 0)?.longVersionCode?.and(0xFFFFFFFF)?.toInt() ?: 0
            else
                pm?.getPackageInfo(pkg, 0)?.versionCode ?: 0

        } else {
            0
        }
    }

    private fun compareInt(int1: Int?, int2: Int?): ButtonTag {
        if (int2 != null && int1 != null) {
            return when {
                int1 == 0 -> ButtonTag.INSTALL
                int2 > int1 -> ButtonTag.UPDATE
                int1 >= int2 -> ButtonTag.REINSTALL
                else -> ButtonTag.INSTALL
            }
        }
        return ButtonTag.INSTALL
    }
}