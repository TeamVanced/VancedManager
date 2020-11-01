package com.vanced.manager.ui.viewmodels

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.crowdin.platform.Crowdin
import com.google.android.material.button.MaterialButton
import com.vanced.manager.R
import com.vanced.manager.core.App
import com.vanced.manager.model.DataModel
import com.vanced.manager.ui.dialogs.AppDownloadDialog
import com.vanced.manager.ui.dialogs.InstallationFilesDetectedDialog
import com.vanced.manager.ui.dialogs.VancedPreferencesDialog
import com.vanced.manager.ui.events.Event
import com.vanced.manager.utils.AppUtils.managerPkg
import com.vanced.manager.utils.AppUtils.microgPkg
import com.vanced.manager.utils.AppUtils.musicPkg
import com.vanced.manager.utils.AppUtils.musicRootPkg
import com.vanced.manager.utils.AppUtils.vancedPkg
import com.vanced.manager.utils.AppUtils.vancedRootPkg
import com.vanced.manager.utils.Extensions.show
import com.vanced.manager.utils.InternetTools
import com.vanced.manager.utils.PackageHelper.apkExist
import com.vanced.manager.utils.PackageHelper.musicApkExists
import com.vanced.manager.utils.PackageHelper.musicRootApkExists
import com.vanced.manager.utils.PackageHelper.uninstallApk
import com.vanced.manager.utils.PackageHelper.vancedInstallFilesExist
import com.vanced.manager.utils.PackageHelper.vancedRootInstallFilesExist

open class HomeViewModel(private val activity: FragmentActivity): ViewModel() {
    
    private val app = activity.application as App

    private val prefs = getDefaultSharedPreferences(activity)

    val vanced = ObservableField<DataModel>()
    val vancedRoot = ObservableField<DataModel>()
    val microg = ObservableField<DataModel>()
    val music = ObservableField<DataModel>()
    val musicRoot = ObservableField<DataModel>()
    val manager = ObservableField<DataModel>()
    val fetching = ObservableBoolean(true)

    private var _navigateDestination = MutableLiveData<Event<Int>>()

    val navigateDestination : LiveData<Event<Int>> = _navigateDestination

    fun fetchData() {
        fetching.set(true)
        app.loadJsonAsync()
        Crowdin.forceUpdate(activity)
        fetching.set(false)
    }
    
    //private val microgSnackbar = Snackbar.make(, R.string.no_microg, Snackbar.LENGTH_LONG).setAction(R.string.install) { downloadMicrog(activity) }
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
            if (app == activity.getString(R.string.vanced))
                VancedPreferencesDialog().show(activity)
            else
                AppDownloadDialog(app).show(activity)

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
                    "nonroot" -> if (musicApkExists(activity)) InstallationFilesDetectedDialog(app).show(activity) else AppDownloadDialog(app).show(activity)
                    "root" -> AppDownloadDialog(app).show(activity)
                }
            }
            activity.getString(R.string.microg) -> {
                Log.d("test", apkExist(activity, "microg.apk").toString())
                if (apkExist(activity, "microg.apk")) InstallationFilesDetectedDialog(app).show(activity) else AppDownloadDialog(app).show(activity)
            }
        }

    }

    fun uninstallPackage(pkg: String) = uninstallApk(pkg, activity)

    init {
        fetching.set(true)
        vanced.set(DataModel(app.vanced, activity, vancedPkg, activity.getString(R.string.vanced), ContextCompat.getDrawable(activity, R.drawable.ic_vanced)))
        vancedRoot.set(DataModel(app.vanced, activity, vancedRootPkg, activity.getString(R.string.vanced), ContextCompat.getDrawable(activity, R.drawable.ic_vanced)))
        music.set(DataModel(app.music, activity, musicPkg, activity.getString(R.string.music), ContextCompat.getDrawable(activity, R.drawable.ic_music)))
        musicRoot.set(DataModel(app.music, activity, musicRootPkg, activity.getString(R.string.music), ContextCompat.getDrawable(activity, R.drawable.ic_music)))
        microg.set(DataModel(app.microg, activity, microgPkg, activity.getString(R.string.microg), ContextCompat.getDrawable(activity, R.drawable.ic_microg)))
        manager.set(DataModel(app.manager, activity, managerPkg, activity.getString(R.string.app_name), ContextCompat.getDrawable(activity, R.mipmap.ic_launcher)))
        fetching.set(false)
    }

}
