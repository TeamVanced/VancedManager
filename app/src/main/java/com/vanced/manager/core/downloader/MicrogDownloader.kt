package com.vanced.manager.core.downloader

import android.content.Context
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.vanced.manager.R
import com.vanced.manager.utils.DownloadHelper.downloadProgress
import com.vanced.manager.utils.InternetTools.getFileNameFromUrl
import com.vanced.manager.utils.InternetTools.microg
import com.vanced.manager.utils.PackageHelper.install
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object MicrogDownloader : CoroutineScope by CoroutineScope(Dispatchers.IO) {

    fun downloadMicrog(
        context: Context,
    ) = launch {
        val url = microg.get()?.string("url")

        downloadProgress.value?.currentDownload = PRDownloader.download(url, context.getExternalFilesDir("microg")?.path, "microg.apk")
            .build()
            .setOnStartOrResumeListener {
                downloadProgress.value?.downloadingFile?.value = context.getString(R.string.downloading_file, url?.let { getFileNameFromUrl(it) })
            }
            .setOnProgressListener { progress ->
                downloadProgress.value?.downloadProgress?.value = (progress.currentBytes * 100 / progress.totalBytes).toInt()
            }
            .start(object : OnDownloadListener {
                override fun onDownloadComplete() {
                    startMicrogInstall(context)
                }

                override fun onError(error: Error?) {
                    downloadProgress.value?.downloadingFile?.value = context.getString(R.string.error_downloading, "microG")
                }
            })

        }

    fun startMicrogInstall(context: Context) {
        downloadProgress.value?.installing?.value = true
        downloadProgress.value?.reset()
        install("${context.getExternalFilesDir("microg")}/microg.apk", context)
    }
}
