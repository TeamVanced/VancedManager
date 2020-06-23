package com.vanced.manager.ui.viewmodels

import android.app.Application
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.vanced.manager.R
import com.vanced.manager.utils.InternetTools.displayJsonString
import com.vanced.manager.utils.PackageHelper.isPackageInstalled

class HomeViewModel(application: Application): AndroidViewModel(application) {

    private val vancedPkgName: String =
        if (getDefaultSharedPreferences(application).getString("vanced_variant", "nonroot") == "root") {
            "com.google.android.youtube"
        } else {
            "com.vanced.android.youtube"
        }

    val microgInstalled: Boolean = isPackageInstalled("com.mgoogle.android.gms", application.packageManager)
    val vancedInstalled: Boolean = isPackageInstalled(vancedPkgName, application.packageManager)

    val vancedInstalledVersion: MutableLiveData<String> = MutableLiveData()
    val microgInstalledVersion: MutableLiveData<String> = MutableLiveData()

    val vancedVersion: MutableLiveData<String> = MutableLiveData()
    val microgVersion: MutableLiveData<String> = MutableLiveData()

    val isNonrootModeSelected: Boolean = getDefaultSharedPreferences(application).getString("vanced_variant", "nonroot") == "nonroot"

    private val signatureString = application.getString(R.string.unavailable)
    val signatureStatusTxt: MutableLiveData<String> = MutableLiveData()

    fun openMicrogSettings() {
        try {
            val intent = Intent()
            intent.component = ComponentName(
                "com.mgoogle.android.gms",
                "org.microg.gms.ui.SettingsActivity"
            )
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(getApplication(), intent, null)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(getApplication(), "App not installed", Toast.LENGTH_SHORT).show()
        }
    }

    fun openUrl(Url: String) {
        val color: Int =
            when (Url) {
                "https://discord.gg/TUVd7rd" -> R.color.Discord
                "https://t.me/joinchat/AAAAAEHf-pi4jH1SDlAL4w" -> R.color.Telegram
                "https://twitter.com/YTVanced" -> R.color.Twitter
                "https://reddit.com/r/vanced" -> R.color.Reddit
                "https://vanced.app" -> R.color.Vanced
                "https://brave.com/van874" -> R.color.Brave
                else -> R.color.Vanced
            }

        val builder = CustomTabsIntent.Builder()
        builder.setToolbarColor(ContextCompat.getColor(getApplication(), color))
        val customTabsIntent = builder.build()
        customTabsIntent.intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        customTabsIntent.launchUrl(getApplication(), Uri.parse(Url))
    }

    private fun getPkgInfo(toCheck: Boolean, pkg: String, application: Application): String  {
        return if (toCheck) {
            application.packageManager.getPackageInfo(pkg, 0).versionName
        } else {
            application.getString(R.string.unavailable)
        }
    }

    init {
        signatureStatusTxt.value = signatureString
        vancedVersion.value = displayJsonString("vanced.json","version", application)
        microgVersion.value = displayJsonString("microg.json","version", application)
        vancedInstalledVersion.value = getPkgInfo(vancedInstalled, vancedPkgName, application)
        microgInstalledVersion.value = getPkgInfo(microgInstalled, "com.mgoogle.android.gms", application)

    }

}