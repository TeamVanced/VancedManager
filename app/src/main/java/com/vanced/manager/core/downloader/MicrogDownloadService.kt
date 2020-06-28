package com.vanced.manager.core.downloader

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.dezlum.codelabs.getjson.GetJson
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.OnStartOrResumeListener
import com.downloader.PRDownloader
import com.vanced.manager.ui.fragments.HomeFragment
import com.vanced.manager.utils.InternetTools.getFileNameFromUrl
import com.vanced.manager.utils.NotificationHelper
import java.lang.Exception
import java.lang.IllegalStateException
import java.lang.RuntimeException
import java.util.concurrent.ExecutionException

class MicrogDownloadService: Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        try {
            downloadMicrog()
        } catch (e: Exception) {
            when (e) {
                is ExecutionException, is InterruptedException -> Toast.makeText(this, "Unable to download Vanced", Toast.LENGTH_SHORT).show()
                else -> throw e
            }
        }
        stopSelf()
        return START_STICKY
    }

    private fun downloadMicrog() {
        val prefs = getSharedPreferences("installPrefs", Context.MODE_PRIVATE)
        prefs?.edit()?.putBoolean("isMicrogDownloading", true)?.apply()

        val apkUrl = GetJson().AsJSONObject("https://x1nto.github.io/VancedFiles/microg.json")
        val dwnldUrl = apkUrl.get("url").asString
        val channel = 420
        PRDownloader.download(dwnldUrl, filesDir.path, "microg.apk")
            .build()
            .setOnStartOrResumeListener { OnStartOrResumeListener { TODO("Not yet implemented") } }
            .setOnProgressListener { progress ->
                val intent = Intent(HomeFragment.MICROG_DOWNLOADING)
                val mProgress = progress.currentBytes * 100 / progress.totalBytes
                intent.action = HomeFragment.MICROG_DOWNLOADING
                intent.putExtra("microgProgress", mProgress.toInt())
                intent.putExtra("fileName", "Downloading ${getFileNameFromUrl(dwnldUrl)}...")
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
                NotificationHelper.displayDownloadNotif(
                    channel,
                    maxVal = 0,
                    filename = getFileNameFromUrl(dwnldUrl),
                    context = this@MicrogDownloadService
                )
            }
            .start(object : OnDownloadListener {
                override fun onDownloadComplete() {
                    val intent = Intent(HomeFragment.MICROG_DOWNLOADED)
                    intent.action = HomeFragment.MICROG_DOWNLOADED
                    LocalBroadcastManager.getInstance(this@MicrogDownloadService).sendBroadcast(intent)
                    prefs?.edit()?.putBoolean("isMicrogDownloading", false)?.apply()
                    NotificationHelper.displayDownloadNotif(
                        channel,
                        maxVal = 0,
                        filename = getFileNameFromUrl(dwnldUrl),
                        context = this@MicrogDownloadService
                    )
                }
                override fun onError(error: Error) {
                    val intent = Intent(HomeFragment.DOWNLOAD_ERROR)
                    intent.action = HomeFragment.DOWNLOAD_ERROR
                    intent.putExtra("DownloadError", error.toString())
                    LocalBroadcastManager.getInstance(this@MicrogDownloadService).sendBroadcast(intent)
                    NotificationHelper.displayDownloadNotif(
                        channel,
                        maxVal = 0,
                        filename = getFileNameFromUrl(dwnldUrl),
                        context = this@MicrogDownloadService
                    )
                }
            })
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}