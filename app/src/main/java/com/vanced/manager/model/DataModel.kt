package com.vanced.manager.model

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.core.content.ContextCompat
import com.beust.klaxon.JsonObject
import com.vanced.manager.R
import com.vanced.manager.utils.InternetTools.baseUrl
import com.vanced.manager.utils.InternetTools.getObjectFromJson
import com.vanced.manager.utils.PackageHelper.isPackageInstalled
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

open class DataModel(
    private val jsonObject: JsonObject,
    variant: String = "nonroot",
    private val context: Context
) {
    
    private val appPkg = 
        when (jsonObject.toString()) {
            "vanced" -> if (variant == "root") "com.google.android.youtube" else "com.vanced.android.youtube"
            "microg" -> "com.mgoogle.android.gms"
            else -> "com.vanced.android.apps.youtube.music"
        }
    
    /*
    private var versionName: String = ""
    private var installedVersionName: String = ""
    private var changelog: String = ""
        
    private var versionCode: Int = 0
    private var installedVersionCode: Int = 0
    */
    
    open fun isAppInstalled(): Boolean = isPackageInstalled(appPkg, context.packageManager)
    
    open fun getVersionName(): String = jsonObject.string("version")!!
    
    open fun getVersionCode(): Int = jsonObject.int("versionCode")!!
    
    open fun getInstalledVersionName(): String = runBlocking(Dispatchers.IO) {
        getPkgVersionName(isAppInstalled(), appPkg)
    }
    
    open fun getInstalledVersionCode(): Int = runBlocking(Dispatchers.IO) {
        getPkgVersionCode(isAppInstalled(), appPkg)
    }
    
    open fun getButtonTxt(): String = compareInt(getInstalledVersionCode(), getVersionCode())
    
    open fun getButtonIcon(): Drawable? = compareIntDrawable(getInstalledVersionCode(), getVersionCode())

    open fun getChangelog(): String = runBlocking(Dispatchers.IO) {
        when (jsonObject.toString()) {
            "vanced" -> getObjectFromJson("$baseUrl/changelog/${getVersionName().replace('.', '_')}.json", "message")
            "music" -> jsonObject.string("changelog")!!
            else -> getObjectFromJson("https://ytvanced.github.io/VancedBackend/$jsonObject.json", "changelog")
        }
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
