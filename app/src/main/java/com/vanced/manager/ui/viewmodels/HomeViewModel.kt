package com.vanced.manager.ui.viewmodels

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.crowdin.platform.Crowdin
import com.google.android.material.button.MaterialButton
import com.vanced.manager.R
import com.vanced.manager.adapter.LinkAdapter.Companion.DISCORD
import com.vanced.manager.adapter.LinkAdapter.Companion.REDDIT
import com.vanced.manager.adapter.LinkAdapter.Companion.TELEGRAM
import com.vanced.manager.adapter.LinkAdapter.Companion.TWITTER
import com.vanced.manager.adapter.SponsorAdapter.Companion.BRAVE
import com.vanced.manager.model.DataModel
import com.vanced.manager.model.RootDataModel
import com.vanced.manager.ui.dialogs.AppDownloadDialog
import com.vanced.manager.ui.dialogs.InstallationFilesDetectedDialog
import com.vanced.manager.ui.dialogs.MusicPreferencesDialog
import com.vanced.manager.ui.dialogs.VancedPreferencesDialog
import com.vanced.manager.utils.*
import com.vanced.manager.utils.AppUtils.managerPkg
import com.vanced.manager.utils.AppUtils.microgPkg
import com.vanced.manager.utils.AppUtils.musicPkg
import com.vanced.manager.utils.AppUtils.musicRootPkg
import com.vanced.manager.utils.AppUtils.vancedPkg
import com.vanced.manager.utils.AppUtils.vancedRootPkg
import com.vanced.manager.utils.PackageHelper.apkExist
import com.vanced.manager.utils.PackageHelper.musicApkExists
import com.vanced.manager.utils.PackageHelper.uninstallApk
import com.vanced.manager.utils.PackageHelper.uninstallRootApk
import com.vanced.manager.utils.PackageHelper.vancedInstallFilesExist
import kotlinx.coroutines.launch

open class HomeViewModel(private val activity: FragmentActivity): ViewModel() {

    private val prefs = getDefaultSharedPreferences(activity)
    private val variant get() = prefs.getString("vanced_variant", "nonroot")

    val vancedModel = MutableLiveData<DataModel>()
    val vancedRootModel = MutableLiveData<RootDataModel>()
    val microgModel = MutableLiveData<DataModel>()
    val musicModel = MutableLiveData<DataModel>()
    val musicRootModel = MutableLiveData<RootDataModel>()
    val managerModel = MutableLiveData<DataModel>()

    fun fetchData() {
        viewModelScope.launch {
            loadJson(activity)
            Crowdin.forceUpdate(activity)
        }
    }
    
    private val microgToast = Toast.makeText(activity, R.string.no_microg, Toast.LENGTH_LONG)

    fun openUrl(url: String) {
        val color: Int =
            when (url) {
                DISCORD -> R.color.Discord
                TELEGRAM -> R.color.Telegram
                TWITTER -> R.color.Twitter
                REDDIT -> R.color.Reddit
                BRAVE -> R.color.Brave
                else -> R.color.Vanced
            }
            
        openUrl(url, color, activity)
    }

    fun launchApp(app: String, isRoot: Boolean) {
        val componentName = when (app) {
            activity.getString(R.string.vanced) -> if (isRoot) ComponentName(vancedRootPkg, "$vancedRootPkg.HomeActivity") else ComponentName(vancedPkg, "$vancedRootPkg.HomeActivity")
            activity.getString(R.string.music) -> if (isRoot) ComponentName(musicRootPkg, "$musicRootPkg.activities.MusicActivity") else ComponentName(musicPkg, "$musicRootPkg.activities.MusicActivity")
            activity.getString(R.string.microg) -> ComponentName(microgPkg, "org.microg.gms.ui.SettingsActivity")
            else -> throw IllegalArgumentException("Can't open this app")
        }
        try {
            activity.startActivity(Intent().setComponent(componentName))
        } catch (e: ActivityNotFoundException) {
            Log.d("VMHMV", e.toString())
        }

    }

    fun openInstallDialog(view: View, app: String) {
        if (variant == "nonroot" && app != activity.getString(R.string.microg) && !microgModel.value?.isAppInstalled?.value!!) {
            microgToast.show()
            return
        }

        if ((view as MaterialButton).text == activity.getString(R.string.update)) {
            when (app) {
                activity.getString(R.string.vanced) -> VancedPreferencesDialog().show(activity)
                activity.getString(R.string.music) -> MusicPreferencesDialog().show(activity)
                else ->  AppDownloadDialog.newInstance(app).show(activity)
            }

            return
        }

        when (app) {
            activity.getString(R.string.vanced) -> {
                when (variant) {
                    "nonroot" -> {
                        if (vancedInstallFilesExist(activity)) {
                            InstallationFilesDetectedDialog.newInstance(app).show(activity)
                        } else {
                            VancedPreferencesDialog().show(activity)
                        }
                    }
                    "root" -> {
                        VancedPreferencesDialog().show(activity)
                    }
                }
            }
            activity.getString(R.string.music) -> {
                when (variant) {
                    "nonroot" -> {
                        if (musicApkExists(activity)) {
                            InstallationFilesDetectedDialog.newInstance(app).show(activity)
                        } else {
                            MusicPreferencesDialog().show(activity)
                        }
                    }
                    "root" -> {
                        MusicPreferencesDialog().show(activity)
                    }
                }
            }
            activity.getString(R.string.microg) -> {
                if (apkExist(activity, "microg.apk")) {
                    InstallationFilesDetectedDialog.newInstance(app).show(activity)
                } else {
                    AppDownloadDialog.newInstance(app).show(activity)
                }
            }
        }

    }

    fun uninstallPackage(pkg: String) {
        if (variant == "root" && uninstallRootApk(pkg)) {
            viewModelScope.launch { loadJson(activity) }
        } else {
            uninstallApk(pkg, activity)
        }
    }

    init {
        if (variant == "root") {
            vancedRootModel.value = RootDataModel(vanced, activity, vancedRootPkg, activity.getString(R.string.vanced), AppCompatResources.getDrawable(activity, R.drawable.ic_vanced), "vanced")
            musicRootModel.value = RootDataModel(music, activity, musicRootPkg, activity.getString(R.string.music), AppCompatResources.getDrawable(activity, R.drawable.ic_music), "music")
        } else {
            vancedModel.value = DataModel(vanced, activity, vancedPkg, activity.getString(R.string.vanced), AppCompatResources.getDrawable(activity, R.drawable.ic_vanced))
            musicModel.value = DataModel(music, activity, musicPkg, activity.getString(R.string.music), AppCompatResources.getDrawable(activity, R.drawable.ic_music))
            microgModel.value = DataModel(microg, activity, microgPkg, activity.getString(R.string.microg), AppCompatResources.getDrawable(activity, R.drawable.ic_microg))
        }
        managerModel.value = DataModel(manager, activity, managerPkg, activity.getString(R.string.app_name), AppCompatResources.getDrawable(activity, R.mipmap.ic_launcher))
    }
}
