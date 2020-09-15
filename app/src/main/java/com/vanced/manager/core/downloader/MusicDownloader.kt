package com.vanced.manager.core.downloader

import android.content.Context
import android.widget.Toast
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.vanced.manager.R
import com.vanced.manager.ui.viewmodels.HomeViewModel.Companion.musicProgress
import com.vanced.manager.utils.AppUtils.installing
import com.vanced.manager.utils.InternetTools.getFileNameFromUrl
import com.vanced.manager.utils.InternetTools.getJsonString
import com.vanced.manager.utils.PackageHelper.install
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object MusicDownloader {

    //private var downloadId: Long = 0

    fun downloadMusic(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val version = getJsonString("music.json", "version", context)
            val url = "https://vanced.app/api/v1/music/v$version.apk"

            //downloadId = download(url, "apk", "music.apk", this@MusicDownloadService)

            PRDownloader.download(url, context.getExternalFilesDir("apk")?.path, "music.apk")
                .build()
                .setOnStartOrResumeListener { 
                    installing = true 
                    musicProgress.get()?.setDownloadingFile(getFileNameFromUrl(url))
                    musicProgress.get()?.showDownloadBar = true
                }
                .setOnProgressListener { progress ->
                    musicProgress.get()?.setDownloadProgress((progress.currentBytes * 100 / progress.totalBytes).toInt())
                }
                .start(object : OnDownloadListener {
                    override fun onDownloadComplete() {
                        install("music", "${context.getExternalFilesDir("apk")}/music.apk", context)
                        musicProgress.get()?.showDownloadBar = false
                        musicProgress.get()?.showInstallCircle = true
                    }

                    override fun onError(error: Error?) {
                        installing = false
                        Toast.makeText(context, context.getString(R.string.error_downloading, "Music"), Toast.LENGTH_SHORT).show()
                    }
                })

        }

    }

    /*
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1) == downloadId) {
                //prefs?.edit()?.putBoolean("isMusicDownloading", false)?.apply()
                //cancelNotif(channel, this@MusicDownloadService)
                val bIntent = Intent(this@MusicDownloadService, AppInstaller::class.java)
                bIntent.putExtra("path", "${getExternalFilesDir("apk")}/music.apk")
                bIntent.putExtra("pkg", "com.mgoogle.android.gms")
                startService(bIntent)
            }
        }
    }
     */

}
