package com.vanced.manager.ui.viewmodels

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.ObservableField
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.crowdin.platform.Crowdin
import com.google.android.material.button.MaterialButton
import com.vanced.manager.R
import com.vanced.manager.model.DataModel
import com.vanced.manager.ui.dialogs.AppDownloadDialog
import com.vanced.manager.ui.dialogs.InstallationFilesDetectedDialog
import com.vanced.manager.ui.dialogs.MusicPreferencesDialog
import com.vanced.manager.ui.dialogs.VancedPreferencesDialog
import com.vanced.manager.utils.AppUtils.managerPkg
import com.vanced.manager.utils.AppUtils.microgPkg
import com.vanced.manager.utils.AppUtils.musicPkg
import com.vanced.manager.utils.AppUtils.musicRootPkg
import com.vanced.manager.utils.AppUtils.vancedPkg
import com.vanced.manager.utils.AppUtils.vancedRootPkg
import com.vanced.manager.utils.Extensions.fetchData
import com.vanced.manager.utils.Extensions.setRefreshing
import com.vanced.manager.utils.Extensions.show
import com.vanced.manager.utils.InternetTools
import com.vanced.manager.utils.InternetTools.loadJson
import com.vanced.manager.utils.PackageHelper.apkExist
import com.vanced.manager.utils.PackageHelper.musicApkExists
import com.vanced.manager.utils.PackageHelper.uninstallApk
import com.vanced.manager.utils.PackageHelper.uninstallRootApk
import com.vanced.manager.utils.PackageHelper.vancedInstallFilesExist
import kotlinx.coroutines.launch

open class HomeViewModel(private val activity: FragmentActivity): ViewModel() {

    private val prefs = getDefaultSharedPreferences(activity)

    val vanced = ObservableField<DataModel>()
    val vancedRoot = ObservableField<DataModel>()
    val microg = ObservableField<DataModel>()
    val music = ObservableField<DataModel>()
    val musicRoot = ObservableField<DataModel>()
    val manager = ObservableField<DataModel>()

    fun fetchData() {
        viewModelScope.launch {
            activity.setRefreshing(true)
            loadJson(activity)
            Crowdin.forceUpdate(activity)
            activity.setRefreshing(false)
        }
    }
    
    private val microgToast = Toast.makeText(activity, R.string.no_microg, Toast.LENGTH_LONG)

    fun openMicrogSettings() {
        try {
            val intent = Intent()
            intent.component = ComponentName(
                "com.mgoogle.android.gms",
                "org.microg.gms.ui.SettingsActivity"
            )
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(activity, intent, null)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show()
        }
    }

    fun openUrl(url: String) {
        val color: Int =
            when (url) {
                "https://discord.gg/TUVd7rd" -> R.color.Discord
                "https://t.me/joinchat/AAAAAEHf-pi4jH1SDlAL4w" -> R.color.Telegram
                "https://twitter.com/YTVanced" -> R.color.Twitter
                "https://reddit.com/r/vanced" -> R.color.Reddit
                "https://vanced.activity" -> R.color.Vanced
                "https://brave.com/van874" -> R.color.Brave
                else -> R.color.Vanced
            }
            
        InternetTools.openUrl(url, color, activity)
    }

    fun openInstallDialog(view: View, app: String) {
        val variant = prefs.getString("vanced_variant", "nonroot")
        if (variant == "nonroot" && app != activity.getString(R.string.microg) && !microg.get()?.isAppInstalled?.get()!!) {
            microgToast.show()
            return
        }

        if ((view as MaterialButton).text == activity.getString(R.string.update)) {
            when (app) {
                activity.getString(R.string.vanced) -> VancedPreferencesDialog().show(activity)
                activity.getString(R.string.music) -> MusicPreferencesDialog().show(activity)
                else ->  AppDownloadDialog(app).show(activity)
            }

            return
        }

        when (app) {
            activity.getString(R.string.vanced) -> {
                when (variant) {
                    "nonroot" -> if (vancedInstallFilesExist(activity)) InstallationFilesDetectedDialog(app).show(activity) else VancedPreferencesDialog().show(activity)
                    "root" -> VancedPreferencesDialog().show(activity)
                }
            }
            activity.getString(R.string.music) -> {
                when (variant) {
                    "nonroot" -> if (musicApkExists(activity)) InstallationFilesDetectedDialog(app).show(activity) else MusicPreferencesDialog().show(activity)
                    "root" -> MusicPreferencesDialog().show(activity)
                }
            }
            activity.getString(R.string.microg) -> {
                if (apkExist(activity, "microg.apk")) InstallationFilesDetectedDialog(app).show(activity) else AppDownloadDialog(app).show(activity)
            }
        }

    }

    fun uninstallPackage(pkg: String) {
        if (prefs.getString("vanced_variant", "nonroot") == "root" && uninstallRootApk(pkg)) {
            viewModelScope.launch { activity.fetchData() }
        } else {
            uninstallApk(pkg, activity)
        }
    }

    init {
        activity.setRefreshing(true)
        vanced.set(DataModel(InternetTools.vanced, activity, vancedPkg, activity.getString(R.string.vanced), AppCompatResources.getDrawable(activity, R.drawable.ic_vanced)))
        vancedRoot.set(DataModel(InternetTools.vanced, activity, vancedRootPkg, activity.getString(R.string.vanced), AppCompatResources.getDrawable(activity, R.drawable.ic_vanced)))
        music.set(DataModel(InternetTools.music, activity, musicPkg, activity.getString(R.string.music), AppCompatResources.getDrawable(activity, R.drawable.ic_music)))
        musicRoot.set(DataModel(InternetTools.music, activity, musicRootPkg, activity.getString(R.string.music), AppCompatResources.getDrawable(activity, R.drawable.ic_music)))
        microg.set(DataModel(InternetTools.microg, activity, microgPkg, activity.getString(R.string.microg), AppCompatResources.getDrawable(activity, R.drawable.ic_microg)))
        manager.set(DataModel(InternetTools.manager, activity, managerPkg, activity.getString(R.string.app_name), AppCompatResources.getDrawable(activity, R.mipmap.ic_launcher)))
        activity.setRefreshing(false)
    }
}
