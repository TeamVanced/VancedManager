package com.vanced.manager.core.downloader

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.preference.PreferenceManager
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.vanced.manager.R
import com.vanced.manager.core.installer.AppInstaller
import com.vanced.manager.ui.fragments.HomeFragment
import com.vanced.manager.utils.InternetTools.baseUrl
import com.vanced.manager.utils.InternetTools.getFileNameFromUrl
import com.vanced.manager.utils.InternetTools.getObjectFromJson
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MicrogDownloadService: Service() {

    //private var downloadId: Long = 0
    private val localBroadcastManager by lazy { LocalBroadcastManager.getInstance(this) }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        downloadMicrog()
        stopSelf()
        return START_NOT_STICKY
    }

    private fun downloadMicrog() {
        runBlocking {
            launch {
                getExternalFilesDir("apk")?.delete()
                val url = getObjectFromJson(
                    "${PreferenceManager.getDefaultSharedPreferences(this@MicrogDownloadService)
                        .getString("install_url", baseUrl)}/microg.json", "url"
                )

                //downloadId = download(url, "apk", "microg.apk", this@MicrogDownloadService)

                PRDownloader.download(url, getExternalFilesDir("apk")?.path, "microg.apk")
                    .build()
                    .setOnProgressListener { progress ->
                        val mProgress = progress.currentBytes * 100 / progress.totalBytes
                        localBroadcastManager.sendBroadcast(Intent(HomeFragment.MICROG_DOWNLOADING).putExtra("progress", mProgress.toInt()).putExtra("file", getFileNameFromUrl(url)))
                    }
                    .start(object : OnDownloadListener {
                        override fun onDownloadComplete() {
                            val intent = Intent(this@MicrogDownloadService, AppInstaller::class.java)
                            intent.putExtra("path", "${getExternalFilesDir("apk")}/microg.apk")
                            intent.putExtra("pkg", "com.mgoogle.android.gms")
                            localBroadcastManager.sendBroadcast(Intent(HomeFragment.MICROG_INSTALLING))
                            startService(intent)
                        }

                        override fun onError(error: Error?) {
                            Toast.makeText(this@MicrogDownloadService, getString(R.string.error_downloading, "microG"), Toast.LENGTH_SHORT).show()
                        }
                    })

            }
        }

    }

    /*
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1) == downloadId) {
                //prefs?.edit()?.putBoolean("isMicrogDownloading", false)?.apply()
                //cancelNotif(channel, this@MicrogDownloadService)
                val bIntent = Intent(this@MicrogDownloadService, AppInstaller::class.java)
                bIntent.putExtra("path", "${getExternalFilesDir("apk")}/microg.apk")
                bIntent.putExtra("pkg", "com.mgoogle.android.gms")
                startService(bIntent)
            }
        }
    }
     */


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}