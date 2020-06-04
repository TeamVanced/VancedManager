package com.vanced.manager.ui.viewmodels

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
import com.vanced.manager.R

open class HomeViewModel(application: Application): AndroidViewModel(application) {

    private fun isPackageInstalled(packageName: String, packageManager: PackageManager): Boolean {
        return try {
            packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    private val vancedPkgName: String =
        if (getDefaultSharedPreferences(application).getString("vanced_variant", "Nonroot") == "Nonroot") {
            "com.google.android.youtube"
        } else {
            "com.vanced.android.youtube"
        }

    val isMicrogInstalled: Boolean = isPackageInstalled("com.mgoogle.android.gms", application.packageManager)
    val isVancedInstalled: Boolean = isPackageInstalled(vancedPkgName, application.packageManager)


    val isConnected = GetJson().isConnected(application)

    val vancedInstalledVersion: String =
        if (isVancedInstalled) {
            application.packageManager.getPackageInfo(vancedPkgName, 0).versionName
        } else {
            application.getString(R.string.unavailable)
        }


    val microgInstalledVersion: String =
        if (isMicrogInstalled) {
            application.packageManager.getPackageInfo("com.mgoogle.android.gms", 0).versionName
        } else {
            application.getString(R.string.unavailable)
        }

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