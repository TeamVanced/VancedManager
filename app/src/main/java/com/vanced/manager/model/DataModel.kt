package com.vanced.manager.model

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.beust.klaxon.JsonObject
import com.vanced.manager.R
import com.vanced.manager.utils.PackageHelper.isPackageInstalled

open class DataModel(
    private val jsonObject: LiveData<JsonObject?>,
    private val context: Context,
    lifecycleOwner: LifecycleOwner,
    val appPkg: String,
    val appName: String,
    val appIcon: Drawable?,
) {

    private val versionCode = MutableLiveData<Int>()
    private val installedVersionCode = MutableLiveData<Int>()

    val isAppInstalled = MutableLiveData<Boolean>()
    val versionName = MutableLiveData<String>()
    val installedVersionName = MutableLiveData<String>()
    val buttonTxt = MutableLiveData<String>()
    val changelog = MutableLiveData<String>()

    private fun fetch() {
        val jobj = jsonObject.value
        isAppInstalled.value = isAppInstalled(appPkg)
        versionCode.value = jobj?.int("versionCode") ?: 0
        versionName.value = jobj?.string("version")?.removeSuffix("-vanced") ?: context.getString(R.string.unavailable)
        changelog.value = jobj?.string("changelog") ?: context.getString(R.string.unavailable)
    }

    init {
        fetch()
        with(lifecycleOwner) {
            jsonObject.observe(this) {
                fetch()
            }
            isAppInstalled.observe(this) {
                installedVersionCode.value = getPkgVersionCode(appPkg)
                installedVersionName.value = getPkgVersionName(appPkg)
            }
            versionCode.observe(this) { versionCode ->
                installedVersionCode.observe(this) { installedVersionCode ->
                    buttonTxt.value = compareInt(installedVersionCode, versionCode)
                }
            }
        }
    }

    open fun isAppInstalled(pkg: String): Boolean = isPackageInstalled(pkg, context.packageManager)

    private fun getPkgVersionName(pkg: String): String {
        val pm = context.packageManager
        return if (isAppInstalled.value == true) {
            pm?.getPackageInfo(pkg, 0)?.versionName?.removeSuffix("-vanced") ?: context.getString(R.string.unavailable)
        } else {
            context.getString(R.string.unavailable)
        }
    }

    @Suppress("DEPRECATION")
    private fun getPkgVersionCode(pkg: String): Int {
        val pm = context.packageManager
        return if (isAppInstalled.value == true) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                pm?.getPackageInfo(pkg, 0)?.longVersionCode?.and(0xFFFFFFFF)?.toInt() ?: 0
            else
                pm?.getPackageInfo(pkg, 0)?.versionCode ?: 0
        } else 0
    }

    private fun compareInt(int1: Int?, int2: Int?): String {
        if (int2 != null && int1 != null) {
            return when {
                int1 == 0 -> context.getString(R.string.install)
                int2 > int1 -> context.getString(R.string.update)
                int2 == int1 || int1 > int2 -> context.getString(R.string.button_reinstall)
                else -> context.getString(R.string.install)
            }
        }
        return context.getString(R.string.install)
    }
}