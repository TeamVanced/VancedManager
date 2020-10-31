package com.vanced.manager.core.downloader

import android.content.Context
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.vanced.manager.R
import com.vanced.manager.core.App
import com.vanced.manager.utils.DownloadHelper.downloadProgress
import com.vanced.manager.utils.InternetTools.baseUrl
import com.vanced.manager.utils.InternetTools.getFileNameFromUrl
import com.vanced.manager.utils.PackageHelper.install
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object MusicDownloader {

    private var variant: String? = null

    fun downloadMusic(context: Context){
        CoroutineScope(Dispatchers.IO).launch {
            val prefs = getDefaultSharedPreferences(context)
            variant = prefs.getString("vanced_variant", "nonroot")
            val url = "${prefs.getString("install_url", baseUrl)}/music/v${(context.applicationContext as App).music.get()?.string("version")}.apk"

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
        install("${context.getExternalFilesDir("music/$variant")}/music.apk", context)
    }

}
