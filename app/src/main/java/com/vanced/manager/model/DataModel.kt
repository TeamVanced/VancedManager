package com.vanced.manager.model

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.beust.klaxon.JsonObject
import com.vanced.manager.R
import com.vanced.manager.utils.PackageHelper.isPackageInstalled
import com.vanced.manager.utils.lifecycleOwner
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
        isAppInstalled.postValue(isAppInstalled(appPkg))
        versionCode.postValue(jobj?.int("versionCode") ?: 0)
        versionName.postValue(jobj?.string("version")?.removeSuffix("-vanced") ?: context.getString(R.string.unavailable))
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
            this?.let {
                isAppInstalled.observe(it) {
                    installedVersionCode.value = getPkgVersionCode(appPkg)
                    installedVersionName.value = getPkgVersionName(appPkg)
                }
            }
            this?.let {
                versionCode.observe(it) { versionCode ->
                    installedVersionCode.observe(it) { installedVersionCode ->
                        buttonTxt.value = compareInt(installedVersionCode, versionCode)
                    }
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