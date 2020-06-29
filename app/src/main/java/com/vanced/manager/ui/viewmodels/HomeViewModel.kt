package com.vanced.manager.ui.viewmodels

import android.app.Application
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Intent
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.dezlum.codelabs.getjson.GetJson
import com.vanced.manager.R
import com.vanced.manager.utils.InternetTools.displayJsonInt
import com.vanced.manager.utils.InternetTools.displayJsonString
import com.vanced.manager.utils.PackageHelper.isPackageInstalled

class HomeViewModel(application: Application): AndroidViewModel(application) {

    private val variant = getDefaultSharedPreferences(application).getString("vanced_variant", "nonroot")
    private val connected: Boolean = GetJson().isConnected(application)

    private val vancedPkgName: String =
        if (variant== "root") {
            "com.google.android.youtube"
        } else {
            "com.vanced.android.youtube"
        }

    private val pm = application.packageManager

    val vancedInstallButtonTxt: MutableLiveData<String> = MutableLiveData()
    val vancedInstallButtonIcon: MutableLiveData<Drawable> = MutableLiveData()

    val microgInstalled: Boolean = isPackageInstalled("com.mgoogle.android.gms", application.packageManager)
    val vancedInstalled: Boolean = isPackageInstalled(vancedPkgName, application.packageManager)

    val vancedInstalledVersion: MutableLiveData<String> = MutableLiveData()
    val microgInstalledVersion: MutableLiveData<String> = MutableLiveData()

    val vancedVersion: MutableLiveData<String> = MutableLiveData()
    val microgVersion: MutableLiveData<String> = MutableLiveData()

    private val vancedInstalledVersionCode = getPkgVerCode(vancedInstalled, vancedPkgName)
    private val microgInstalledVersionCode = getPkgVerCode(microgInstalled, "com.mgoogle.android.gms")

    private val vancedVersionCode = displayJsonInt("vanced.json", "versionCode", application)
    private val microgVersionCode = displayJsonInt("microg.json", "versionCode", application)

    val microgInstallButtonTxt = compareInt(microgInstalledVersionCode, microgVersionCode, application)

    val microgInstallButtonIcon = compareIntDrawable(microgInstalledVersionCode, microgVersionCode, application)

    val nonrootModeSelected: Boolean = variant == "nonroot"

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
            pm.getPackageInfo(pkg, 0).versionName
        } else {
            application.getString(R.string.unavailable)
        }
    }

    private fun getPkgVerCode(toCheck: Boolean, pkg: String): Int {
        return if (toCheck) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                pm.getPackageInfo(pkg, 0).longVersionCode.and(0xFFFFFFFF).toInt()
            else
                pm.getPackageInfo(pkg, 0).versionCode
        } else 0
    }

    private fun compareInt(int1: Int, int2: Int, application: Application): String {
        return if (connected)
            when {
                int1 > int2 -> application.getString(R.string.update)
                int2 == int1 -> application.getString(R.string.button_reinstall)
                int2 == 0 -> application.getString(R.string.install)
                else -> application.getString(R.string.install)
        } else application.getString(R.string.install)
    }

    private fun compareIntDrawable(int1: Int, int2: Int, application: Application): Drawable? {
        return if (connected)
            when {
                int1 > int2 -> application.getDrawable(R.drawable.ic_update)
                int2 == int1 -> application.getDrawable(R.drawable.ic_done)
                int2 == 0 -> application.getDrawable(R.drawable.ic_download)
                else -> application.getDrawable(R.drawable.ic_download)
        } else application.getDrawable(R.drawable.ic_download)

    }

    init {
        vancedVersion.value = displayJsonString("vanced.json","version", application)
        microgVersion.value = displayJsonString("microg.json","version", application)
        vancedInstalledVersion.value = getPkgInfo(vancedInstalled, vancedPkgName, application)
        microgInstalledVersion.value = getPkgInfo(microgInstalled, "com.mgoogle.android.gms", application)
        vancedInstallButtonIcon.value =
            if (variant == "nonroot") {
                if (microgInstalled)
                    compareIntDrawable(vancedVersionCode, vancedInstalledVersionCode, application)
                else
                    null
            } else
                compareIntDrawable(vancedVersionCode, vancedInstalledVersionCode, application)

        vancedInstallButtonTxt.value =
            if (variant == "nonroot") {
                if (microgInstalled) {
                    compareInt(vancedVersionCode, vancedInstalledVersionCode, application)
                } else {
                    application.getString(R.string.no_microg)
                }
            } else
                compareInt(vancedVersionCode, vancedInstalledVersionCode, application)

    }

}