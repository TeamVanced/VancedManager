package com.vanced.manager.core.downloader

import android.content.Context
import android.widget.Toast
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.vanced.manager.R
import com.vanced.manager.core.App
import com.vanced.manager.ui.viewmodels.HomeViewModel.Companion.microgProgress
import com.vanced.manager.utils.AppUtils.mutableInstall
import com.vanced.manager.utils.InternetTools.getFileNameFromUrl
import com.vanced.manager.utils.PackageHelper.install
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object MicrogDownloader {

    fun downloadMicrog(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val url = (context.applicationContext as App).microg.get()?.string("url")

             microgProgress.get()?.currentDownload = PRDownloader.download(url, context.getExternalFilesDir("apk")?.path, "microg.apk")
                .build()
                .setOnStartOrResumeListener { 
                    mutableInstall.value = true
                    microgProgress.get()?.downloadingFile?.set(context.getString(R.string.downloading_file, url?.let { getFileNameFromUrl(it) }))
                    microgProgress.get()?.showDownloadBar?.set(true)
                }
                 .setOnCancelListener {
                     mutableInstall.value = false
                     microgProgress.get()?.showDownloadBar?.set(false)
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
                        mutableInstall.value = false
                        microgProgress.get()?.showDownloadBar?.set(false)
                        Toast.makeText(context, context.getString(R.string.error_downloading, "microG"), Toast.LENGTH_SHORT).show()
                    }
                })

        }
    }

}
