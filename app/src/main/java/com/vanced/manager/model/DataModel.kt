package com.vanced.manager.model

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.beust.klaxon.JsonObject
import com.vanced.manager.R
import com.vanced.manager.utils.Extensions.lifecycleOwner
import com.vanced.manager.utils.PackageHelper.isPackageInstalled
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

open class DataModel(
    private val jsonObject: LiveData<JsonObject?>,
    private val context: Context,
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

    private fun fetch() = CoroutineScope(Dispatchers.IO).launch {
        val jobj = jsonObject.value
        isAppInstalled.postValue(isPackageInstalled(appPkg, context.packageManager))
        versionCode.postValue(jobj?.int("versionCode") ?: 0)
        installedVersionCode.postValue(getPkgVersionCode(isAppInstalled.value, appPkg))
        versionName.postValue(jobj?.string("version")?.removeSuffix("-vanced") ?: context.getString(
                R.string.unavailable
            ))
        installedVersionName.postValue(getPkgVersionName(isAppInstalled.value, appPkg))
        changelog.postValue(jobj?.string("changelog") ?: context.getString(R.string.unavailable))
    }

    init {
        fetch()
        with(context.lifecycleOwner()) {
            this?.let {
                jsonObject.observe(it) {
                    fetch()
                }
            }
            this?.let { versionCode.observe(it) { versionCode ->
                installedVersionCode.observe(it) { installedVersionCode ->
                    buttonTxt.value = compareInt(installedVersionCode, versionCode)
                }
            }}
        }
    }

    private fun getPkgVersionName(toCheck: Boolean?, pkg: String): String {
        val pm = context.packageManager
        return if (toCheck == true) {
            pm.getPackageInfo(pkg, 0).versionName.removeSuffix("-vanced")
        } else {
            context.getString(R.string.unavailable)
        }
    }

    @Suppress("DEPRECATION")
    private fun getPkgVersionCode(toCheck: Boolean?, pkg: String): Int {
        return if (toCheck == true) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                context.packageManager.getPackageInfo(pkg, 0).longVersionCode.and(0xFFFFFFFF)
                    .toInt()
            else
                context.packageManager.getPackageInfo(pkg, 0).versionCode
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