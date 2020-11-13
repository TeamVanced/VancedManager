package com.vanced.manager.core.downloader

import android.content.Context
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.vanced.manager.R
import com.vanced.manager.utils.AppUtils.musicRootPkg
import com.vanced.manager.utils.AppUtils.validateTheme
import com.vanced.manager.utils.DeviceUtils.getArch
import com.vanced.manager.utils.DownloadHelper.downloadProgress
import com.vanced.manager.utils.Extensions.getInstallUrl
import com.vanced.manager.utils.Extensions.getLatestAppVersion
import com.vanced.manager.utils.InternetTools.backupUrl
import com.vanced.manager.utils.InternetTools.getFileNameFromUrl
import com.vanced.manager.utils.InternetTools.music
import com.vanced.manager.utils.InternetTools.musicVersions
import com.vanced.manager.utils.PackageHelper.downloadStockCheck
import com.vanced.manager.utils.PackageHelper.install
import com.vanced.manager.utils.PackageHelper.installMusicRoot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object MusicDownloader {

    private var variant: String? = null
    private var version: String? = null
    private var versionCode: Int? = null
    private var baseurl = ""
    private var downloadPath: String? = null
    private var hashUrl: String? = null

    fun downloadMusic(context: Context) {
        val prefs = getDefaultSharedPreferences(context)
        version = prefs.getString("music_version", "latest")?.getLatestAppVersion(musicVersions.get()?.value ?: listOf(""))
        versionCode = music.get()?.int("versionCode")
        variant = prefs.getString("vanced_variant", "nonroot")
        baseurl = "${prefs.getInstallUrl()}/music/v$version"
        downloadPath = context.getExternalFilesDir("music/$variant")?.path
        hashUrl = "$baseurl/hash.json"

        downloadApk(context)
    }

    private fun downloadApk(context: Context, apk: String = "music") {
        CoroutineScope(Dispatchers.IO).launch {
            val url =
                    if (apk == "stock")
                        "$baseurl/stock/${getArch()}.apk"
                    else
                        "$baseurl/$variant.apk"

            downloadProgress.get()?.currentDownload = PRDownloader.download(url, downloadPath, getFileNameFromUrl(url))
                .build()
                .setOnStartOrResumeListener {
                    downloadProgress.get()?.downloadingFile?.set(context.getString(R.string.downloading_file, getFileNameFromUrl(url)))
                }
                .setOnProgressListener { progress ->
                    downloadProgress.get()?.downloadProgress?.set((progress.currentBytes * 100 / progress.totalBytes).toInt())
                }
                .start(object : OnDownloadListener {
                    override fun onDownloadComplete() {
                        if (variant == "root" && apk != "stock") {
                            downloadApk(context, "stock")
                            return
                        }

                        when (apk) {
                            "music" -> {
                                if (variant == "root") {
                                    if (validateTheme(downloadPath!!, "root", hashUrl!!, context)) {
                                        if (downloadStockCheck(musicRootPkg, versionCode!!, context))
                                            downloadApk(context, "stock")
                                        else
                                            startMusicInstall(context)
                                    } else {
                                        downloadApk(context, apk)
                                    }
                                } else
                                    startMusicInstall(context)
                            }
                        }

                        startMusicInstall(context)
                    }

                    override fun onError(error: Error?) {
                        if (baseurl != backupUrl) {
                            baseurl = "$backupUrl/music/v$version"
                            downloadApk(context, apk)
                            return
                        }

                        downloadProgress.get()?.downloadingFile?.set(context.getString(R.string.error_downloading, "Music"))
                    }
                })

        }

    }

    fun startMusicInstall(context: Context) {
        downloadProgress.get()?.installing?.set(true)
        downloadProgress.get()?.reset()
        if (variant == "root")
            installMusicRoot(context)
        else
            install("${context.getExternalFilesDir("music/nonroot")}/nonroot.apk", context)
    }

}
