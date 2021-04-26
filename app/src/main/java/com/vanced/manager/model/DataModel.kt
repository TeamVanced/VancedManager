package com.vanced.manager.model

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
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
    val appDescription: String,
    @DrawableRes val appIcon: Int
) {

    private val versionCode = MutableLiveData<Int>()
    private val installedVersionCode = MutableLiveData<Int>()
    private val unavailable = context.getString(R.string.unavailable)
    private val pm = context.packageManager

    val isAppInstalled = MutableLiveData<Boolean>()
    val versionName = MutableLiveData<String>()
    val installedVersionName = MutableLiveData<String>()
    val buttonTag = MutableLiveData<ButtonTag>()
    val buttonImage = MutableLiveData<Drawable>()
    val changelog = MutableLiveData<String>()

    private fun fetch() {
        val jobj = jsonObject.value
        isAppInstalled.value = isAppInstalled(appPkg)
        versionCode.value = jobj?.int("versionCode") ?: 0
        versionName.value = jobj?.string("version") ?: unavailable
        changelog.value = jobj?.string("changelog") ?: unavailable
    }

    init {
        fetch()
        with(lifecycleOwner) {
            jsonObject.observe(this) {
                fetch()
            }
            isAppInstalled.observe(this) {
                installedVersionCode.value = getPkgVersionCode(appPkg, it)
                installedVersionName.value = getPkgVersionName(appPkg, it)
            }
            versionCode.observe(this) { versionCode ->
                installedVersionCode.observe(this) { installedVersionCode ->
                    buttonTag.value = compareInt(installedVersionCode, versionCode)
                    buttonImage.value = compareIntDrawable(installedVersionCode, versionCode)
                }
            }
        }
    }

    open fun isAppInstalled(pkg: String): Boolean = isPackageInstalled(pkg, context.packageManager)

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

    private fun compareIntDrawable(int1: Int?, int2: Int?): Drawable {
        if (int2 != null && int1 != null) {
            return when {
                int1 == 0 -> ContextCompat.getDrawable(context, R.drawable.ic_app_download)!!
                int2 > int1 -> ContextCompat.getDrawable(context, R.drawable.ic_app_update)!!
                int1 >= int2 -> ContextCompat.getDrawable(context, R.drawable.ic_app_reinstall)!!
                else -> ContextCompat.getDrawable(context, R.drawable.ic_app_download)!!
            }
        }
        return ContextCompat.getDrawable(context, R.drawable.ic_app_download)!!
    }
}