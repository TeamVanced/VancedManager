package com.vanced.manager.core.downloader

import android.content.Context
import android.widget.Toast
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.vanced.manager.R
import com.vanced.manager.ui.viewmodels.HomeViewModel.Companion.microgProgress
import com.vanced.manager.utils.AppUtils.installing
import com.vanced.manager.utils.InternetTools.getFileNameFromUrl
import com.vanced.manager.utils.InternetTools.getJsonString
import com.vanced.manager.utils.PackageHelper.install
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object MicrogDownloader {

    //private var downloadId: Long = 0
    
    fun downloadMicrog(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val url = getJsonString("microg.json", "url", context)

            //downloadId = download(url, "apk", "microg.apk", this@MicrogDownloadService)

            PRDownloader.download(url, context.getExternalFilesDir("apk")?.path, "microg.apk")
                .build()
                .setOnStartOrResumeListener { 
                    installing = true
                    microgProgress.get()?.downloadingFile?.set(context.getString(R.string.downloading_file, getFileNameFromUrl(url)))
                    microgProgress.get()?.showDownloadBar?.set(true)
                }
                .setOnProgressListener { progress ->
                    microgProgress.get()?.downloadProgress?.set((progress.currentBytes * 100 / progress.totalBytes).toInt())
                }
                .start(object : OnDownloadListener {
                    override fun onDownloadComplete() {
                        install("microg", "${context.getExternalFilesDir("apk")}/microg.apk", context)
                        microgProgress.get()?.showDownloadBar?.set(false)
                        microgProgress.get()?.showInstallCircle?.set(true)
                    }

                    override fun onError(error: Error?) {
                        installing = false
                        microgProgress.get()?.showDownloadBar?.set(false)
                        Toast.makeText(context, context.getString(R.string.error_downloading, "microG"), Toast.LENGTH_SHORT).show()
                    }
                })

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

}
