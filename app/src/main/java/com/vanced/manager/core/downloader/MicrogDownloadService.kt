package com.vanced.manager.core.downloader

import android.app.DownloadManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.IBinder
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.preference.PreferenceManager
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.OnStartOrResumeListener
import com.downloader.PRDownloader
import com.vanced.manager.R
import com.vanced.manager.core.installer.AppInstaller
import com.vanced.manager.ui.fragments.HomeFragment
import com.vanced.manager.utils.InternetTools.baseUrl
import com.vanced.manager.utils.InternetTools.getFileNameFromUrl
import com.vanced.manager.utils.InternetTools.getObjectFromJson
import com.vanced.manager.utils.NotificationHelper
import com.vanced.manager.utils.NotificationHelper.cancelNotif
import com.vanced.manager.utils.NotificationHelper.createBasicNotif
import java.io.File

class MicrogDownloadService: Service() {

    private var downloadId: Long = 0

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        downloadMicrog()
        stopSelf()
        return START_NOT_STICKY
    }

    private fun downloadMicrog() {
        //val prefs = getSharedPreferences("installPrefs", Context.MODE_PRIVATE)
        val apkUrl = getObjectFromJson("${PreferenceManager.getDefaultSharedPreferences(this).getString("install_url", baseUrl)}/microg.json", "url", this)

        val request = DownloadManager.Request(Uri.parse(apkUrl))
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
        request.setTitle(getString(R.string.downloading_file, "MicroG"))
        request.setDestinationUri(Uri.fromFile(File("${filesDir.path}/microg.apk")))

        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadId = downloadManager.enqueue(request)

        /*
        val channel = 420
        PRDownloader.download(apkUrl, filesDir.path, "microg.apk")
            .build()
            .setOnStartOrResumeListener { OnStartOrResumeListener { prefs?.edit()?.putBoolean("isMicrogDownloading", true)?.apply() } }
            .setOnProgressListener { progress ->
                val mProgress = progress.currentBytes * 100 / progress.totalBytes
                NotificationHelper.displayDownloadNotif(
                    channel,
                    mProgress.toInt(),
                    getFileNameFromUrl(apkUrl),
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
                    val mIntent = Intent(HomeFragment.MICROG_DOWNLOADED)
                    mIntent.action = HomeFragment.MICROG_DOWNLOADED
                    LocalBroadcastManager.getInstance(this@MicrogDownloadService).sendBroadcast(mIntent)
                    startService(intent)
                }
                override fun onError(error: Error) {
                    prefs?.edit()?.putBoolean("isMicrogDownloading", false)?.apply()
                    createBasicNotif(getString(R.string.error_downloading, "Microg"), channel, this@MicrogDownloadService)
                }
            })

         */
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1) == downloadId) {
                //prefs?.edit()?.putBoolean("isMicrogDownloading", false)?.apply()
                //cancelNotif(channel, this@MicrogDownloadService)
                val bIntent = Intent(this@MicrogDownloadService, AppInstaller::class.java)
                bIntent.putExtra("path", "${filesDir.path}/microg.apk")
                bIntent.putExtra("pkg", "com.mgoogle.android.gms")
                val mIntent = Intent(HomeFragment.MICROG_DOWNLOADED)
                mIntent.action = HomeFragment.MICROG_DOWNLOADED
                LocalBroadcastManager.getInstance(this@MicrogDownloadService).sendBroadcast(mIntent)
                startService(bIntent)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cancelNotif(420, this)
        unregisterReceiver(receiver)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}