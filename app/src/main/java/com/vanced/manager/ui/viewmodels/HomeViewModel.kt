package com.vanced.manager.ui.viewmodels

import android.app.Application
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.vanced.manager.R
import com.vanced.manager.adapter.LinkAdapter.Companion.DISCORD
import com.vanced.manager.adapter.LinkAdapter.Companion.REDDIT
import com.vanced.manager.adapter.LinkAdapter.Companion.TELEGRAM
import com.vanced.manager.adapter.LinkAdapter.Companion.TWITTER
import com.vanced.manager.adapter.SponsorAdapter.Companion.BRAVE
import com.vanced.manager.model.ButtonTag
import com.vanced.manager.model.DataModel
import com.vanced.manager.model.RootDataModel
import com.vanced.manager.ui.dialogs.AppDownloadDialog
import com.vanced.manager.ui.dialogs.InstallationFilesDetectedDialog
import com.vanced.manager.ui.dialogs.MusicPreferencesDialog
import com.vanced.manager.ui.dialogs.VancedPreferencesDialog
import com.vanced.manager.utils.*
import com.vanced.manager.utils.AppUtils.log
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

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = getDefaultSharedPreferences(context)
    private val variant get() = prefs.getString("vanced_variant", "nonroot")
    private val context: Context get() = getApplication()

    val vancedModel = MutableLiveData<DataModel>()
    val vancedRootModel = MutableLiveData<RootDataModel>()
    val microgModel = MutableLiveData<DataModel>()
    val musicModel = MutableLiveData<DataModel>()
    val musicRootModel = MutableLiveData<RootDataModel>()
    val managerModel = MutableLiveData<DataModel>()

    fun fetchData() {
        viewModelScope.launch {
            loadJson(context)
        }
    }

    private val microgToast = Toast.makeText(context, R.string.no_microg, Toast.LENGTH_LONG)

    fun openUrl(context: Context, url: String) {
        val color: Int =
            when (url) {
                DISCORD -> R.color.Discord
                TELEGRAM -> R.color.Telegram
                TWITTER -> R.color.Twitter
                REDDIT -> R.color.Reddit
                BRAVE -> R.color.Brave
                else -> R.color.Vanced
            }

        openUrl(url, color, context)
    }

    fun launchApp(app: String, isRoot: Boolean) {
        val componentName = when (app) {
            context.getString(R.string.vanced) -> if (isRoot) ComponentName(
                vancedRootPkg,
                "$vancedRootPkg.HomeActivity"
            ) else ComponentName(vancedPkg, "$vancedRootPkg.HomeActivity")
            context.getString(R.string.music) -> if (isRoot) ComponentName(
                musicRootPkg,
                "$musicRootPkg.activities.MusicActivity"
            ) else ComponentName(musicPkg, "$musicRootPkg.activities.MusicActivity")
            context.getString(R.string.microg) -> ComponentName(
                microgPkg,
                "org.microg.gms.ui.SettingsActivity"
            )
            else -> throw IllegalArgumentException("Can't open this app")
        }
        try {
            context.startActivity(Intent().setComponent(componentName))
        } catch (e: ActivityNotFoundException) {
            log("VMHMV", e.toString())
        }

    }

    fun openInstallDialog(fragmentManager: FragmentManager, buttonTag: ButtonTag?, app: String) {
        if (variant == "nonroot" && app != context.getString(R.string.microg) && !microgModel.value?.isAppInstalled?.value!!) {
            microgToast.show()
            return
        }

        if (buttonTag == ButtonTag.UPDATE) {
            when (app) {
                context.getString(R.string.vanced) -> VancedPreferencesDialog().show(fragmentManager)
                context.getString(R.string.music) -> MusicPreferencesDialog().show(fragmentManager)
                else -> AppDownloadDialog.newInstance(app).show(fragmentManager)
            }
            return
        }

        when (app) {
            context.getString(R.string.vanced) -> {
                when (variant) {
                    "nonroot" -> {
                        if (vancedInstallFilesExist(context)) {
                            InstallationFilesDetectedDialog.newInstance(app).show(fragmentManager)
                        } else {
                            VancedPreferencesDialog().show(fragmentManager)
                        }
                    }
                    "root" -> {
                        VancedPreferencesDialog().show(fragmentManager)
                    }
                }
            }
            context.getString(R.string.music) -> {
                when (variant) {
                    "nonroot" -> {
                        if (musicApkExists(context)) {
                            InstallationFilesDetectedDialog.newInstance(app).show(fragmentManager)
                        } else {
                            MusicPreferencesDialog().show(fragmentManager)
                        }
                    }
                    "root" -> {
                        MusicPreferencesDialog().show(fragmentManager)
                    }
                }
            }
            context.getString(R.string.microg) -> {
                if (apkExist(context, "microg.apk")) {
                    InstallationFilesDetectedDialog.newInstance(app).show(fragmentManager)
                } else {
                    AppDownloadDialog.newInstance(app).show(fragmentManager)
                }
            }
        }

    }

    fun uninstallPackage(pkg: String) {
        if (variant == "root" && uninstallRootApk(pkg)) {
            viewModelScope.launch { loadJson(context) }
        } else {
            uninstallApk(pkg, context)
        }
    }

    init {
        with(context) {
            if (variant == "root") {
                vancedRootModel.value = RootDataModel(
                    vanced,
                    this,
                    vancedRootPkg,
                    this.getString(R.string.vanced),
                    this.getString(R.string.description_vanced),
                    R.drawable.ic_vanced,
                    "vanced"
                )
                musicRootModel.value = RootDataModel(
                    music,
                    this,
                    musicRootPkg,
                    this.getString(R.string.music),
                    this.getString(R.string.description_vanced_music),
                    R.drawable.ic_music,
                    "music"
                )
            } else {
                vancedModel.value = DataModel(
                    vanced,
                    this,
                    vancedPkg,
                    this.getString(R.string.vanced),
                    this.getString(R.string.description_vanced),
                    R.drawable.ic_vanced
                )
                musicModel.value = DataModel(
                    music,
                    this,
                    musicPkg,
                    this.getString(R.string.music),
                    this.getString(R.string.description_vanced_music),
                    R.drawable.ic_music
                )
                microgModel.value = DataModel(
                    microg,
                    this,
                    microgPkg,
                    this.getString(R.string.microg),
                    this.getString(R.string.description_microg),
                    R.drawable.ic_microg
                )
            }
            managerModel.value = DataModel(
                manager,
                this,
                managerPkg,
                this.getString(R.string.app_name),
                "Just manager meh",
                R.mipmap.ic_launcher
            )
        }
    }
}
