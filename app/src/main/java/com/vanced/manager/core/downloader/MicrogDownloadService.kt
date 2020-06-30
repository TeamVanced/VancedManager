package com.vanced.manager.core.downloader

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import com.dezlum.codelabs.getjson.GetJson
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.OnStartOrResumeListener
import com.downloader.PRDownloader
import com.vanced.manager.R
import com.vanced.manager.core.installer.AppInstaller
import com.vanced.manager.utils.InternetTools.getFileNameFromUrl
import com.vanced.manager.utils.NotificationHelper
import com.vanced.manager.utils.NotificationHelper.cancelNotif
import com.vanced.manager.utils.NotificationHelper.createBasicNotif
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
        return START_NOT_STICKY
    }

    private fun downloadMicrog() {
        val prefs = getSharedPreferences("installPrefs", Context.MODE_PRIVATE)

        val apkUrl = GetJson().AsJSONObject("https://x1nto.github.io/VancedFiles/microg.json")
        val dwnldUrl = apkUrl.get("url").asString
        val channel = 420
        PRDownloader.download(dwnldUrl, filesDir.path, "microg.apk")
            .build()
            .setOnStartOrResumeListener { OnStartOrResumeListener { prefs?.edit()?.putBoolean("isMicrogDownloading", true)?.apply() } }
            .setOnProgressListener { progress ->
                val mProgress = progress.currentBytes * 100 / progress.totalBytes
                NotificationHelper.displayDownloadNotif(
                    channel,
                    mProgress.toInt(),
                    getFileNameFromUrl(dwnldUrl),
                    this
                )
            }
            .start(object : OnDownloadListener {
                override fun onDownloadComplete() {
                    prefs?.edit()?.putBoolean("isMicrogDownloading", false)?.apply()
                    cancelNotif(channel, this@MicrogDownloadService)
                    val intent = Intent(this@MicrogDownloadService, AppInstaller::class.java)
                    intent.putExtra("path", "${filesDir.path}/microg.apk")
                    intent.putExtra("pkg", "com.mgoogle.android.gms")
                    startService(intent)
                }
                override fun onError(error: Error) {
                    prefs?.edit()?.putBoolean("isMicrogDownloading", false)?.apply()
                    createBasicNotif(getString(R.string.error_downloading, "Microg"), channel, this@MicrogDownloadService)
                }
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        cancelNotif(420, this)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}