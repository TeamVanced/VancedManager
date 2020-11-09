package com.vanced.manager.core.downloader

import android.content.Context
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.vanced.manager.R
import com.vanced.manager.utils.DeviceUtils.getArch
import com.vanced.manager.utils.DownloadHelper.downloadProgress
import com.vanced.manager.utils.Extensions.getLatestAppVersion
import com.vanced.manager.utils.InternetTools.baseUrl
import com.vanced.manager.utils.InternetTools.getFileNameFromUrl
import com.vanced.manager.utils.InternetTools.music
import com.vanced.manager.utils.InternetTools.musicVersions
import com.vanced.manager.utils.PackageHelper.install
import com.vanced.manager.utils.PackageHelper.installMusicRoot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object MusicDownloader {

    private var variant: String? = null
    private var version: String? = null
    private var baseurl = ""

    fun downloadMusic(context: Context) {
        val prefs = getDefaultSharedPreferences(context)
        version = prefs.getString("music_version", "latest")?.getLatestAppVersion(musicVersions.get()?.value ?: listOf(""))
        variant = prefs.getString("vanced_variant", "nonroot")
        baseurl = "${prefs.getString("install_url", baseUrl)}/music/v$version"

        downloadApk(context)
    }

    private fun downloadApk(context: Context, apk: String = "music") {
        CoroutineScope(Dispatchers.IO).launch {
            val url =
                    if (apk == "stock")
                        "$baseurl/stock/${getArch()}.apk"
                    else
                        "$baseurl/$variant.apk"

            downloadProgress.get()?.currentDownload = PRDownloader.download(url, context.getExternalFilesDir("music/$variant")?.path, "music.apk")
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

                        startMusicInstall(context)
                    }

                    override fun onError(error: Error?) {
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
            install("${context.getExternalFilesDir("music/$variant")}/music.apk", context)
    }

}
