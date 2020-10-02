package com.vanced.manager.model

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.beust.klaxon.JsonObject
import com.vanced.manager.BuildConfig.ENABLE_SIGNATURE_CHECK
import com.vanced.manager.R
import com.vanced.manager.utils.AppUtils.doSignaturesMatch
import com.vanced.manager.utils.AppUtils.managerPkg
import com.vanced.manager.utils.AppUtils.vancedRootPkg
import com.vanced.manager.utils.PackageHelper.isPackageInstalled
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

open class DataModel(
    private val jsonObject: ObservableField<JsonObject?>,
    private val appPkg: String,
    private val context: Context
) {

    private val versionCode = ObservableInt()
    private val installedVersionCode = ObservableInt()

    val isAppInstalled = ObservableBoolean()
    val versionName = ObservableField<String>()
    val installedVersionName = ObservableField<String>()
    val buttonTxt = ObservableField<String>()
    val buttonIcon = ObservableField<Drawable>()
    val changelog = ObservableField<String>()
    val isOfficial = ObservableBoolean()

    fun fetch() = CoroutineScope(Dispatchers.IO).launch {
        isAppInstalled.set(isPackageInstalled(appPkg, context.packageManager))
        isOfficial.set(doSignaturesMatch(managerPkg, appPkg, context))
        versionName.set(jsonObject.get()?.string("version")?.removeSuffix("-vanced") ?: context.getString(R.string.unavailable))
        installedVersionName.set(getPkgVersionName(isAppInstalled.get(), appPkg))
        versionCode.set(jsonObject.get()?.int("versionCode") ?: 0)
        installedVersionCode.set(getPkgVersionCode(isAppInstalled.get(), appPkg))
        buttonTxt.set(compareInt(installedVersionCode.get(), versionCode.get()))
        buttonIcon.set(compareIntDrawable(installedVersionCode.get(), versionCode.get()))
        changelog.set(jsonObject.get()?.string("changelog") ?: context.getString(R.string.unavailable))
    }

    init {
        fetch()
    }
    
    private fun getPkgVersionName(toCheck: Boolean, pkg: String): String  {
        val pm = context.packageManager
        return if (toCheck) {
            if (ENABLE_SIGNATURE_CHECK) {
                if (isOfficial.get() || appPkg == vancedRootPkg)
                    pm.getPackageInfo(pkg, 0).versionName.removeSuffix("-vanced")
                else
                    pm.getPackageInfo(pkg, 0).versionName.removeSuffix("-vanced") + " (${context.getString(R.string.unofficial)})"
            } else
                pm.getPackageInfo(pkg, 0).versionName.removeSuffix("-vanced")
        } else {
            context.getString(R.string.unavailable)
        }
    }

    /*
    private fun doSignaturesMatch(pkg1: String, pkg2: String): Boolean =
        if (isPackageInstalled(pkg2, context.packageManager))
            context.packageManager.checkSignatures(pkg1, pkg2) == PackageManager.SIGNATURE_MATCH
        else
            true

     */

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
