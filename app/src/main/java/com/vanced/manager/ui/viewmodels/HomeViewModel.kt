package com.vanced.manager.ui.viewmodels

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.crowdin.platform.Crowdin
import com.downloader.PRDownloader
import com.downloader.Status
import com.topjohnwu.superuser.Shell
import com.vanced.manager.R
import com.vanced.manager.core.App
import com.vanced.manager.core.downloader.MicrogDownloader.downloadMicrog
import com.vanced.manager.core.downloader.MusicDownloader.downloadMusic
import com.vanced.manager.core.downloader.VancedDownloader.downloadVanced
import com.vanced.manager.model.DataModel
import com.vanced.manager.model.ProgressModel
import com.vanced.manager.ui.events.Event
import com.vanced.manager.utils.AppUtils.installing
import com.vanced.manager.utils.InternetTools
import com.vanced.manager.utils.PackageHelper.uninstallApk

open class HomeViewModel(private val activity: Activity): ViewModel() {
    
    private val app = activity.application as App

    //val variant = getDefaultSharedPreferences(activity).getString("vanced_variant", "nonroot")

    val vanced = ObservableField<DataModel>()
    val vancedRoot = ObservableField<DataModel>()
    val microg = ObservableField<DataModel>()
    val music = ObservableField<DataModel>()
    val manager = ObservableField<DataModel>()
    val fetching = ObservableBoolean()

    private var _navigateDestination = MutableLiveData<Event<Int>>()

    val navigateDestination : LiveData<Event<Int>> = _navigateDestination

    fun fetchData() {
        fetching.set(true)
        app.loadJsonAsync()
        vanced.get()?.fetch()
        vancedRoot.get()?.fetch()
        music.get()?.fetch()
        microg.get()?.fetch()
        manager.get()?.fetch()
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

    fun installVanced(variant: String) {
        if (!installing.value!!) {
            if (!fetching.get()) {
                when {
                    variant == "nonroot" && !microg.get()?.isAppInstalled?.get()!! -> microgToast.show()
                    variant == "root" && !Shell.rootAccess() -> Toast.makeText(activity, R.string.root_not_granted, Toast.LENGTH_SHORT).show()
                    else -> {
                        if (activity.getSharedPreferences("installPrefs", Context.MODE_PRIVATE).getBoolean("valuesModified", false)) {
                            downloadVanced(activity)
                        } else {
                            _navigateDestination.value = Event(R.id.toInstallThemeFragment)
                        }
                    }
                }
            }
        } else
            Toast.makeText(activity, R.string.installation_wait, Toast.LENGTH_SHORT).show()
    }
    
    fun installMusic() {
        if (!installing.value!!) {
            if (!fetching.get()) {
                if (!microg.get()?.isAppInstalled?.get()!!) {
                    microgToast.show()
                } else {
                    downloadMusic(activity)
                }
            }
        } else
            Toast.makeText(activity, R.string.installation_wait, Toast.LENGTH_SHORT).show()
    }
    
    fun installMicrog() {
        if (!installing.value!!)
            downloadMicrog(activity)
        else
            Toast.makeText(activity, R.string.installation_wait, Toast.LENGTH_SHORT).show()
    }
    
    fun uninstallVanced(variant: String) = uninstallApk(if (variant == "root") "com.google.android.youtube" else "com.vanced.android.youtube", activity)
    fun uninstallMusic() = uninstallApk("com.vanced.android.apps.youtube.music", activity)
    fun uninstallMicrog() = uninstallApk("com.mgoogle.android.gms", activity)

    fun cancelDownload(downloadId: Int) {
        PRDownloader.cancel(downloadId)
    }

    fun pauseResumeDownload(downloadId: Int) {
        if (PRDownloader.getStatus(downloadId) == Status.PAUSED)
            PRDownloader.resume(downloadId)
        else
            PRDownloader.pause(downloadId)
    }
    
    companion object {
        val vancedProgress = ObservableField<ProgressModel>()
        val musicProgress = ObservableField<ProgressModel>()
        val microgProgress = ObservableField<ProgressModel>()
    }

    init {
        fetching.set(true)
        vanced.set(DataModel(app.vanced, "com.vanced.android.youtube", activity))
        vancedRoot.set(DataModel(app.vanced, "com.google.android.youtube", activity))
        music.set(DataModel(app.music, "com.vanced.android.apps.youtube.music", activity))
        microg.set(DataModel(app.microg, "com.mgoogle.android.gms", activity))
        manager.set(DataModel(app.manager, "com.vanced.manager", activity))
        vancedProgress.set(ProgressModel())
        musicProgress.set(ProgressModel())
        microgProgress.set(ProgressModel())
        fetching.set(false)
    }

}
