package com.vanced.manager.ui.viewmodel

import android.content.pm.PackageInstaller
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vanced.manager.downloader.base.AppDownloader
import com.vanced.manager.downloader.impl.MicrogDownloader
import com.vanced.manager.downloader.impl.MusicDownloader
import com.vanced.manager.downloader.impl.VancedDownloader
import com.vanced.manager.installer.impl.MicrogInstaller
import com.vanced.manager.installer.impl.MusicInstaller
import com.vanced.manager.installer.impl.VancedInstaller
import com.vanced.manager.network.util.MICROG_NAME
import com.vanced.manager.network.util.MUSIC_NAME
import com.vanced.manager.network.util.VANCED_NAME
import com.vanced.manager.preferences.holder.managerVariantPref
import com.vanced.manager.repository.manager.PackageManagerResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InstallViewModel(
    private val vancedDownloader: VancedDownloader,
    private val musicDownloader: MusicDownloader,
    private val microgDownloader: MicrogDownloader,

    private val vancedInstaller: VancedInstaller,
    private val musicInstaller: MusicInstaller,
    private val microgInstaller: MicrogInstaller,
) : ViewModel() {

    private val isRoot
        get() = managerVariantPref == "root"

    sealed class Log {
        data class Info(val infoText: String) : Log()
        data class Success(val successText: String) : Log()
        data class Error(
            val displayText: String,
            val stacktrace: String,
        ) : Log()
    }

    sealed class Status {
        object Idle : Status()
        object Installing : Status()
        object Installed : Status()
        object Failure : Status()
        data class Progress(val progress: Float) : Status()
    }

    val logs = mutableStateListOf<Log>()

    var status by mutableStateOf<Status>(Status.Idle)
        private set

    //TODO Move to WorkManager
    fun startAppProcess(
        appName: String,
        appVersions: List<String>?
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            downloadApp(appName, appVersions)
        }
    }

    fun postInstallStatus(pmStatus: Int, extra: String) {
        if (pmStatus == PackageInstaller.STATUS_SUCCESS) {
            status = Status.Installed
            log(Log.Success("Successfully installed"))
        } else {
            status = Status.Failure
            log(Log.Error("Failed to install app", extra))
        }
    }

    fun clear() {
        logs.clear()
        status = Status.Idle
    }

    private suspend fun downloadApp(
        appName: String,
        appVersions: List<String>?,
    ) {
        val downloader = getDownloader(appName)

        val onProgress: (Float) -> Unit = { progress ->
            status = Status.Progress(progress / 100)
        }
        val onFile: (String) -> Unit = { file ->
            log(Log.Info("Downloading $file"))
        }

        val download =
            if (isRoot)
                downloader.downloadRoot(appVersions, onProgress, onFile)
            else
                downloader.download(appVersions, onProgress, onFile)

        when (download) {
            is AppDownloader.DownloadStatus.Success -> {
                log(Log.Success("Successfully downloaded $appName"))
                installApp(appName, appVersions)
            }
            is AppDownloader.DownloadStatus.Error -> {
                log(
                    Log.Error(
                        displayText = "Failed to download ${download.fileName}",
                        stacktrace = download.error
                    )
                )
            }
        }
    }

    private suspend fun installApp(
        appName: String,
        appVersions: List<String>?,
    ) {
        val installer = getInstaller(appName)

        status = Status.Installing

        if (isRoot) {
            when (val installStatus = installer.installRoot(appVersions)) {
                is PackageManagerResult.Success -> {
                    status = Status.Installed
                    log(Log.Success("Successfully installed"))
                }
                is PackageManagerResult.Error -> {
                    status = Status.Failure
                    log(Log.Error("Failed to install app", installStatus.message))
                }
            }
        } else {
            installer.install(appVersions)
        }
    }

    private fun getDownloader(
        appName: String
    ) = when (appName) {
        VANCED_NAME -> vancedDownloader
        MUSIC_NAME -> musicDownloader
        MICROG_NAME -> microgDownloader
        else -> throw IllegalArgumentException("$appName is not a valid app")
    }

    private fun getInstaller(
        appName: String
    ) = when (appName) {
        VANCED_NAME -> vancedInstaller
        MUSIC_NAME -> musicInstaller
        MICROG_NAME -> microgInstaller
        else -> throw IllegalArgumentException("$appName is not a valid app")
    }

    private fun log(data: Log) {
        logs.add(data)
    }

}