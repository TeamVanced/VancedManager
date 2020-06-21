package com.vanced.manager.core.downloader

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.dezlum.codelabs.getjson.GetJson
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.vanced.manager.ui.fragments.HomeFragment
import com.vanced.manager.utils.InternetTools.getFileNameFromUrl

class MicrogDownloadService: Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        downloadMicrog()
        stopSelf()
        return START_NOT_STICKY
    }

    private fun downloadMicrog() {
        val prefs = getSharedPreferences("installPrefs", Context.MODE_PRIVATE)
        prefs?.edit()?.putBoolean("isMicrogDownloading", true)?.apply()

        val apkUrl = GetJson().AsJSONObject("https://x1nto.github.io/VancedFiles/microg.json")
        val dwnldUrl = apkUrl.get("url").asString
        PRDownloader.download(dwnldUrl, filesDir.path, "microg.apk")
            .build()
            .setOnProgressListener { progress ->
                val intent = Intent(HomeFragment.MICROG_DOWNLOADING)
                val mProgress = progress.currentBytes * 100 / progress.totalBytes
                intent.action = HomeFragment.MICROG_DOWNLOADING
                intent.putExtra("microgProgress", mProgress.toInt())
                intent.putExtra("fileName", "Downloading ${getFileNameFromUrl(dwnldUrl)}...")
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
            }
            .start(object : OnDownloadListener {
                override fun onDownloadComplete() {
                    val intent = Intent(HomeFragment.MICROG_DOWNLOADED)
                    intent.action = HomeFragment.MICROG_DOWNLOADED
                    LocalBroadcastManager.getInstance(this@MicrogDownloadService).sendBroadcast(intent)
                    prefs?.edit()?.putBoolean("isMicrogDownloading", false)?.apply()
                }
                override fun onError(error: Error) {
                    val intent = Intent(HomeFragment.DOWNLOAD_ERROR)
                    intent.action = HomeFragment.DOWNLOAD_ERROR
                    intent.putExtra("DownloadError", error.toString())
                    LocalBroadcastManager.getInstance(this@MicrogDownloadService).sendBroadcast(intent)
                }
            })
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}