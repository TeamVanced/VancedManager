package com.vanced.manager.model

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.vanced.manager.utils.InternetTools.baseUrl
import com.vanced.manager.utils.InternetTools.getJsonInt
import com.vanced.manager.utils.InternetTools.getJsonString
import com.vanced.manager.utils.InternetTools.getObjectFromJson
import com.vanced.manager.utils.PackageHelper.isPackageInstalled
import com.vanced.manager.R
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class DataModel(
    private val jsonName: String, 
    private val context: Context
) {
    
    private val variant = getDefaultSharedPreferences(context).getString("vanced_variant", "nonroot")
    
    private val appPkg = 
        when (jsonName) {
            "vanced" -> if (variant == "root") "com.google.android.youtube" else "com.vanced.android.youtube"
            "microg" -> "com.mgoogle.android.gms"
            else -> "com.vanced.android.youtube.music"
        }
    
    /*
    private var versionName: String = ""
    private var installedVersionName: String = ""
    private var changelog: String = ""
        
    private var versionCode: Int = 0
    private var installedVersionCode: Int = 0
    */
    
    open fun isAppInstalled(): Boolean = isPackageInstalled(appPkg, context.packageManager)
    
    open fun getVersionName(): String = runBlocking(Dispatchers.IO) {
        getJsonString("$jsonName.json", "version", context)
    }
    
    open fun getVersionCode(): Int = runBlocking(Dispatchers.IO) {
        getJsonInt("$jsonName.json", "versionCode", context)
    }
    
    open fun getInstalledVersionName(): String = runBlocking(Dispatchers.IO) {
        getPkgVersionName(isAppInstalled(), appPkg)
    }
    
    open fun getInstalledVersionCode(): Int = runBlocking(Dispatchers.IO) {
        getPkgVersionCode(isAppInstalled(), appPkg)
    }
    
    open fun getButtonTxt(): String = compareInt(getInstalledVersionCode(), getVersionCode())
    
    open fun getButtonIcon(): Drawable? = compareIntDrawable(getInstalledVersionCode(), getVersionCode())

    open fun getChangelog(): String = runBlocking(Dispatchers.IO) {
        if (jsonName == "vanced")
            getObjectFromJson("$baseUrl/changelog/${getVersionName().replace('.', '_')}.json", "message")
        else
            getObjectFromJson("https://ytvanced.github.io/VancedBackend/$jsonName.json", "changelog")
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
            int1 == 0 -> context.getDrawable(R.drawable.ic_download)
            int2 > int1 -> context.getDrawable(R.drawable.ic_update)
            int2 == int1 -> context.getDrawable(R.drawable.ic_done)
            else -> context.getDrawable(R.drawable.ic_download)
        }
    }
    
} 
