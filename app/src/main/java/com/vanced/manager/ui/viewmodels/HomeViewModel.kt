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
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.crowdin.platform.Crowdin
import com.vanced.manager.R
import com.vanced.manager.utils.InternetTools.getJsonInt
import com.vanced.manager.utils.InternetTools.getJsonString
import com.vanced.manager.utils.PackageHelper.isPackageInstalled
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

open class HomeViewModel(application: Application): AndroidViewModel(application) {

    private val variant = getDefaultSharedPreferences(application).getString("vanced_variant", "nonroot")

    private val vancedPkgName: String =
        if (variant == "root") {
            "com.google.android.youtube"
        } else {
            "com.vanced.android.youtube"
        }

    private val pm = application.packageManager

    private val vancedInstalledVersionCode = ObservableField<Int>()
    private val microgInstalledVersionCode = ObservableField<Int>()

    private val vancedVersionCode = ObservableField<Int>()
    private val microgVersionCode = ObservableField<Int>()

    //this is fucking retarded
    val vancedInstallButtonTxt = ObservableField<String>()
    val vancedInstallButtonIcon = ObservableField<Drawable>()
    val microgInstalled = ObservableField<Boolean>()
    val vancedInstalled = ObservableField<Boolean>()
    val vancedInstalledVersion = ObservableField<String>()
    val microgInstalledVersion = ObservableField<String>()
    val vancedVersion = ObservableField<String>()
    val microgVersion = ObservableField<String>()
    val microgInstallButtonTxt = ObservableField<String>()
    val microgInstallButtonIcon = ObservableField<Drawable>()

    val nonrootModeSelected: Boolean = variant == "nonroot"

    val fetching = ObservableField<Boolean>()

    val shouldBeDisabled = ObservableField<Boolean>()

    //this too
    fun fetchData() {
        runBlocking {
            launch {
                fetching.set(true)
                Crowdin.forceUpdate(getApplication())
                vancedVersion.set(getJsonString("vanced.json", "version", getApplication()))
                microgVersion.set(getJsonString("microg.json", "version", getApplication()))
                microgInstalled.set(isPackageInstalled("com.mgoogle.android.gms", pm))
                vancedInstalled.set(isPackageInstalled(vancedPkgName, pm))
                vancedInstalledVersion.set(getPkgInfo(vancedInstalled.get()!!, vancedPkgName, getApplication()))
                microgInstalledVersion.set(getPkgInfo(microgInstalled.get()!!, "com.mgoogle.android.gms", getApplication()))
                vancedVersionCode.set(getJsonInt("vanced.json", "versionCode", getApplication()))
                microgVersionCode.set(getJsonInt("microg.json", "versionCode", getApplication()))
                vancedInstalledVersionCode.set(getPkgVerCode(vancedInstalled.get()!!, vancedPkgName))
                microgInstalledVersionCode.set(getPkgVerCode(microgInstalled.get()!!, "com.mgoogle.android.gms"))
                microgInstallButtonTxt.set(compareInt(microgInstalledVersionCode.get()!!, microgVersionCode.get()!!, getApplication()))
                microgInstallButtonIcon.set(compareIntDrawable(microgInstalledVersionCode.get()!!, microgVersionCode.get()!!, getApplication()))
                shouldBeDisabled.set(nonrootModeSelected && !microgInstalled.get()!!)
                vancedInstallButtonIcon.set(
                    if (shouldBeDisabled.get()!!) {
                        null
                    } else
                        compareIntDrawable(vancedInstalledVersionCode.get()!!, vancedVersionCode.get()!!, getApplication())
                )
                vancedInstallButtonTxt.set(
                    if (shouldBeDisabled.get()!!) {
                        getApplication<Application>().getString(R.string.no_microg)
                    } else
                        compareInt(vancedInstalledVersionCode.get()!!, vancedVersionCode.get()!!, getApplication())
                )
                fetching.set(false)
            }
        }
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

    @Suppress("DEPRECATION")
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
            int1 == 0 -> application.getString(R.string.install)
            int2 > int1 -> application.getString(R.string.update)
            int2 == int1 -> application.getString(R.string.button_reinstall)
            else -> application.getString(R.string.install)
        }

    }

    private fun compareIntDrawable(int1: Int, int2: Int, application: Application): Drawable? {
        return when {
            int1 == 0 -> application.getDrawable(R.drawable.ic_download)
            int2 > int1 -> application.getDrawable(R.drawable.ic_update)
            int2 == int1 -> application.getDrawable(R.drawable.ic_done)
            else -> application.getDrawable(R.drawable.ic_download)
        }
    }

    init {
        //expanded.set(false)
        //fetchData()
    }

}