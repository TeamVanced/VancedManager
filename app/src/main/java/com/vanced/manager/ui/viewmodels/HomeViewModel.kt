package com.vanced.manager.ui.viewmodels

import android.app.Application
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Intent
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
import com.vanced.manager.R
import com.vanced.manager.utils.InternetTools.displayJsonInt
import com.vanced.manager.utils.InternetTools.displayJsonString
import com.vanced.manager.utils.PackageHelper.isPackageInstalled

class HomeViewModel(application: Application): AndroidViewModel(application) {

    private val variant = getDefaultSharedPreferences(application).getString("vanced_variant", "nonroot")

    private val vancedPkgName: String =
        if (variant== "root") {
            "com.google.android.youtube"
        } else {
            "com.vanced.android.youtube"
        }

    private val pm = application.packageManager

    val vancedInstallButtonTxt: MutableLiveData<String> = MutableLiveData()
    val vancedInstallButtonIcon: MutableLiveData<Drawable> = MutableLiveData()
    val microgInstalled: MutableLiveData<Boolean> = MutableLiveData()
    val vancedInstalled: MutableLiveData<Boolean> = MutableLiveData()
    val vancedInstalledVersion: MutableLiveData<String> = MutableLiveData()
    val microgInstalledVersion: MutableLiveData<String> = MutableLiveData()
    val vancedVersion: MutableLiveData<String> = MutableLiveData()
    val microgVersion: MutableLiveData<String> = MutableLiveData()
    val microgInstallButtonTxt: MutableLiveData<String> = MutableLiveData()
    val microgInstallButtonIcon: MutableLiveData<Drawable> = MutableLiveData()

    private val vancedInstalledVersionCode: MutableLiveData<Int> = MutableLiveData()
    private val microgInstalledVersionCode: MutableLiveData<Int> = MutableLiveData()

    private val vancedVersionCode: MutableLiveData<Int> = MutableLiveData()
    private val microgVersionCode: MutableLiveData<Int> = MutableLiveData()

    val nonrootModeSelected: Boolean = variant == "nonroot"

    val fetching: MutableLiveData<Boolean> = MutableLiveData()

    fun fetchData() {
        fetching.value = true
        vancedVersion.value = displayJsonString("vanced.json", "version", getApplication())
        microgVersion.value = displayJsonString("microg.json", "version", getApplication())
        microgInstalled.value = isPackageInstalled("com.mgoogle.android.gms", pm)
        vancedInstalled.value = isPackageInstalled(vancedPkgName, pm)
        vancedInstalledVersion.value = getPkgInfo(vancedInstalled.value!!, vancedPkgName, getApplication())
        microgInstalledVersion.value = getPkgInfo(microgInstalled.value!!, "com.mgoogle.android.gms", getApplication())
        vancedVersionCode.value = displayJsonInt("vanced.json", "versionCode", getApplication())
        microgVersionCode.value = displayJsonInt("microg.json", "versionCode", getApplication())
        vancedInstalledVersionCode.value = getPkgVerCode(vancedInstalled.value!!, vancedPkgName)
        microgInstalledVersionCode.value = getPkgVerCode(microgInstalled.value!!, "com.mgoogle.android.gms")
        microgInstallButtonTxt.value = compareInt(microgInstalledVersionCode.value!!, microgVersionCode.value!!, getApplication())
        microgInstallButtonIcon.value = compareIntDrawable(microgInstalledVersionCode.value!!, microgVersionCode.value!!, getApplication())
        vancedInstallButtonIcon.value =
            if (variant == "nonroot") {
                if (microgInstalled.value!!)
                    compareIntDrawable(vancedVersionCode.value!!, vancedInstalledVersionCode.value!!, getApplication())
                else
                    null
            } else
                compareIntDrawable(vancedVersionCode.value!!, vancedInstalledVersionCode.value!!, getApplication())

        vancedInstallButtonTxt.value =
            if (variant == "nonroot") {
                if (microgInstalled.value!!) {
                    compareInt(vancedVersionCode.value!!, vancedInstalledVersionCode.value!!, getApplication())
                } else {
                    getApplication<Application>().getString(R.string.no_microg)
                }
            } else
                compareInt(vancedVersionCode.value!!, vancedInstalledVersionCode.value!!, getApplication())
        fetching.value = false
    }

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
        val customTabPrefs = getDefaultSharedPreferences(getApplication()).getBoolean("use_customtabs", true)
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

        if (customTabPrefs) {
            val builder = CustomTabsIntent.Builder()
            builder.setToolbarColor(ContextCompat.getColor(getApplication(), color))
            val customTabsIntent = builder.build()
            customTabsIntent.intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            customTabsIntent.launchUrl(getApplication(), Uri.parse(Url))
        } else {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(Url))
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(getApplication(), intent , null)
        }
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
        return when {
            int2 == 0 -> application.getString(R.string.install)
            int1 > int2 -> application.getString(R.string.update)
            int2 == int1 -> application.getString(R.string.button_reinstall)
            else -> application.getString(R.string.install)
        }

    }

    private fun compareIntDrawable(int1: Int, int2: Int, application: Application): Drawable? {
        return when {
            int2 == 0 -> application.getDrawable(R.drawable.ic_download)
            int1 > int2 -> application.getDrawable(R.drawable.ic_update)
            int2 == int1 -> application.getDrawable(R.drawable.ic_done)
            else -> application.getDrawable(R.drawable.ic_download)
        }
    }

    init {
        fetchData()
    }

}