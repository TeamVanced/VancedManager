package com.vanced.manager.model

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.beust.klaxon.JsonObject
import com.vanced.manager.R
import com.vanced.manager.utils.PackageHelper.isPackageInstalled

open class DataModel(
    private val jsonObject: JsonObject?,
    variant: String = "nonroot",
    app: String,
    private val context: Context
) {
    
    private val appPkg = 
        when (app) {
            "vanced" -> if (variant == "root") "com.google.android.youtube" else "com.vanced.android.youtube"
            "microg" -> "com.mgoogle.android.gms"
            else -> "com.vanced.android.apps.youtube.music"
        }

    private val versionCode = ObservableInt()
    private val installedVersionCode = ObservableInt()

    val isAppInstalled = ObservableBoolean()
    val versionName = ObservableField<String>()
    val installedVersionName = ObservableField<String>()
    val buttonTxt = ObservableField<String>()
    val buttonIcon = ObservableField<Drawable>()
    val changelog = ObservableField<String>()

    fun fetch() {
        isAppInstalled.set(isPackageInstalled(appPkg, context.packageManager))
        versionName.set(jsonObject?.string("version")?.removeSuffix("-vanced") ?: context.getString(R.string.unavailable))
        installedVersionName.set(getPkgVersionName(isAppInstalled.get(), appPkg))
        versionCode.set(jsonObject?.int("versionCode") ?: 0)
        installedVersionCode.set(getPkgVersionCode(isAppInstalled.get(), appPkg))
        buttonTxt.set(compareInt(installedVersionCode.get(), versionCode.get()))
        buttonIcon.set(compareIntDrawable(installedVersionCode.get(), versionCode.get()))
        changelog.set(jsonObject?.string("changelog") ?: context.getString(R.string.unavailable))
    }

    init {
        fetch()
    }
    
    private fun getPkgVersionName(toCheck: Boolean, pkg: String): String  {
        return if (toCheck) {
            context.packageManager.getPackageInfo(pkg, 0).versionName.removeSuffix("-vanced")
        } else {
            context.getString(R.string.unavailable)
        }
    }

    @Suppress("DEPRECATION")
    private fun getPkgVersionCode(toCheck: Boolean, pkg: String): Int {
        return if (toCheck) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                context.packageManager.getPackageInfo(pkg, 0).longVersionCode.and(0xFFFFFFFF).toInt()
            else
                context.packageManager.getPackageInfo(pkg, 0).versionCode
        } else 0
    }

    private fun compareInt(int1: Int, int2: Int): String {
        return when {
            int1 == 0 -> context.getString(R.string.install)
            int2 > int1 -> context.getString(R.string.update)
            int2 == int1 || int1 > int2 -> context.getString(R.string.button_reinstall)
            else -> context.getString(R.string.install)
        }

    }

    private fun compareIntDrawable(int1: Int, int2: Int): Drawable? {
        return when {
            int1 == 0 -> ContextCompat.getDrawable(context, R.drawable.ic_download)
            int2 > int1 -> ContextCompat.getDrawable(context, R.drawable.ic_update)
            int2 == int1 -> ContextCompat.getDrawable(context, R.drawable.ic_done)
            else -> ContextCompat.getDrawable(context, R.drawable.ic_download)
        }
    }
    
} 
