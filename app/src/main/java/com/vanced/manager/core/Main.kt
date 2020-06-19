package com.vanced.manager.core

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.preference.PreferenceManager
import com.dezlum.codelabs.getjson.GetJson
import com.vanced.manager.core.base.BaseActivity
import com.vanced.manager.ui.core.ThemedActivity
import com.vanced.manager.ui.dialogs.DialogContainer.showSecurityDialog
import com.vanced.manager.ui.dialogs.DialogContainer.statementFalse
import zlc.season.rxdownload4.file

// This activity will NOT be used in manifest
// since MainActivity will extend it
@SuppressLint("Registered")
open class Main: ThemedActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val firstStart = prefs.getBoolean("firstStart", true)
        val isUpgrading = prefs.getBoolean("isUpgrading", false)

        //Easter Egg
        val falseStatement = prefs.getBoolean("statement", true)

        when {
            firstStart -> showSecurityDialog(this)
            !falseStatement -> statementFalse(this)
            isUpgrading -> {
                val apkUrl = GetJson().AsJSONObject("https://x1nto.github.io/VancedFiles/manager.json")
                val dwnldUrl = apkUrl.get("url").asString

                if (dwnldUrl.file().exists())
                    dwnldUrl.file().delete()

                prefs.edit().putBoolean("isUpgrading", false).apply()
            }
        }

    }

    override fun onPause() {
        getSharedPreferences("installPrefs", Context.MODE_PRIVATE).edit().putBoolean("isInstalling", false).apply()
        getSharedPreferences("installPrefs", Context.MODE_PRIVATE).edit().putBoolean("isVancedDownloading", false).apply()
        getSharedPreferences("installPrefs", Context.MODE_PRIVATE).edit().putBoolean("isMicrogDownloading", false).apply()
        try {
            cacheDir.deleteRecursively()
        } catch (e: Exception) {
            Log.d("VMCache", "Unable to delete cacheDir")
        }
        super.onPause()
    }

}