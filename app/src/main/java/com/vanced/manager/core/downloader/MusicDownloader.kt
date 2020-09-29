package com.vanced.manager.core.downloader

import android.content.Context
import android.widget.Toast
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.vanced.manager.R
import com.vanced.manager.core.App
import com.vanced.manager.ui.viewmodels.HomeViewModel.Companion.musicProgress
import com.vanced.manager.utils.AppUtils.mutableInstall
import com.vanced.manager.utils.InternetTools.getFileNameFromUrl
import com.vanced.manager.utils.PackageHelper.install
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object MusicDownloader {

    //private var downloadId: Long = 0

    fun downloadMusic(context: Context){
        CoroutineScope(Dispatchers.IO).launch {
            val url = "https://vanced.app/api/v1/music/v${(context.applicationContext as App).music.get()?.string("version")}.apk"

            //downloadId = download(url, "apk", "music.apk", this@MusicDownloadService)

            musicProgress.get()?.currentDownload = PRDownloader.download(url, context.getExternalFilesDir("apk")?.path, "music.apk")
                .build()
                .setOnStartOrResumeListener { 
                    mutableInstall.value = true
                    musicProgress.get()?.downloadingFile?.set(context.getString(R.string.downloading_file, getFileNameFromUrl(url)))
                    musicProgress.get()?.showDownloadBar?.set(true)
                }
                .setOnProgressListener { progress ->
                    musicProgress.get()?.downloadProgress?.set((progress.currentBytes * 100 / progress.totalBytes).toInt())
                }
                .setOnCancelListener {
                    mutableInstall.value = false
                    musicProgress.get()?.showDownloadBar?.set(false)
                }
                .start(object : OnDownloadListener {
                    override fun onDownloadComplete() {
                        install("music", "${context.getExternalFilesDir("apk")}/music.apk", context)
                        musicProgress.get()?.showDownloadBar?.set(false)
                        musicProgress.get()?.showInstallCircle?.set(true)
                    }

                    override fun onError(error: Error?) {
                        mutableInstall.value = false
                        musicProgress.get()?.showDownloadBar?.set(false)
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
