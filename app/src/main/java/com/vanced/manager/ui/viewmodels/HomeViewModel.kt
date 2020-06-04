package com.vanced.manager.ui.viewmodels

import android.app.Activity
import android.app.Application
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.AndroidViewModel
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.dezlum.codelabs.getjson.GetJson

open class HomeViewModel(application: Application): AndroidViewModel(application) {

    private fun isPackageInstalled(packageName: String, packageManager: PackageManager): Boolean {
        return try {
            packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    val isMicrogInstalled: Boolean = isPackageInstalled("com.mgoogle.android.gms", Activity().packageManager)
    val isVancedInstalled: Boolean = isPackageInstalled("com.vanced.android.youtube", Activity().packageManager)

    val isConnected = GetJson().isConnected(application)

    val vancedInstalledVersion: String =
        if (getDefaultSharedPreferences(application).getString("vanced_variant", "Nonroot") == "Nonroot") {
            Activity().packageManager.getPackageInfo("com.vanced.android.youtube", 0).versionName
        } else {
            Activity().packageManager.getPackageInfo("com.google.android.youtube", 0).versionName
        }

    val microgInstalledVersion: String = Activity().packageManager.getPackageInfo("com.mgoogle.android.gms", 0).versionName

    val vancedVersion: String = GetJson().AsJSONObject("https://x1nto.github.io/VancedFiles/vanced.json")
        .get("version").asString

    val microgVersion: String = GetJson().AsJSONObject("https://x1nto.github.io/VancedFiles/microg.json")
        .get("version").asString

    val isNonrootModeSelected: Boolean = getDefaultSharedPreferences(application).getString("vanced_variant", "Nonroot") == "Nonroot"

    fun openMicrogSettings() {
        try {
            val intent = Intent()
            intent.component = ComponentName(
                "com.mgoogle.android.gms",
                "org.microg.gms.ui.SettingsActivity"
            )
            startActivity(getApplication(), intent, null)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(getApplication(), "App not installed", Toast.LENGTH_SHORT).show()
        }
    }

}