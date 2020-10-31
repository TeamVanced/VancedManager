package com.vanced.manager.core.downloader

import android.content.Context
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.vanced.manager.R
import com.vanced.manager.core.App
import com.vanced.manager.utils.DownloadHelper.downloadProgress
import com.vanced.manager.utils.InternetTools.getFileNameFromUrl
import com.vanced.manager.utils.PackageHelper.install
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object MicrogDownloader {

    fun downloadMicrog(
        context: Context,
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val url = (context.applicationContext as App).microg.get()?.string("url")

             downloadProgress.get()?.currentDownload = PRDownloader.download(url, context.getExternalFilesDir("microg")?.path, "microg.apk")
                .build()
                .setOnStartOrResumeListener {
                    downloadProgress.get()?.downloadingFile?.set(context.getString(R.string.downloading_file, url?.let { getFileNameFromUrl(it) }))
                }
                .setOnProgressListener { progress ->
                    downloadProgress.get()?.downloadProgress?.set((progress.currentBytes * 100 / progress.totalBytes).toInt())
                }
                .start(object : OnDownloadListener {
                    override fun onDownloadComplete() {
                        startMicrogInstall(context)
                    }

                    override fun onError(error: Error?) {
                        downloadProgress.get()?.downloadingFile?.set(context.getString(R.string.error_downloading, "microG"))
                    }
                })

        }
    }

    fun startMicrogInstall(context: Context) {
        downloadProgress.get()?.installing?.set(true)
        downloadProgress.get()?.reset()
        install("${context.getExternalFilesDir("microg")}/microg.apk", context)
    }

}
