package com.vanced.manager.utils

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.content.FileProvider
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.vanced.manager.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

object DownloadHelper {

    fun download(url: String, dir: String, child: String, context: Context): Long {
        val request = DownloadManager.Request(Uri.parse(url)).apply {
            setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
            setTitle(context.getString(R.string.downloading_file, child))
            setDestinationInExternalFilesDir(context, dir, child)
        }

        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        return downloadManager.enqueue(request)
    }

    fun downloadManager(forceUpdate: Boolean, context: Context, loadBar: ProgressBar? = null) {
        CoroutineScope(if (forceUpdate) Dispatchers.IO else Dispatchers.Main).launch {
            val url = "https://github.com/YTVanced/VancedManager/releases/latest/download/manager.apk"
            //downloadId = activity?.let { download(url, "apk", "manager.apk", it) }!!
            PRDownloader.download(url, context.getExternalFilesDir("apk")?.path, "manager.apk")
                .build().apply{
                    if (!forceUpdate) setOnProgressListener {progress ->
                        val mProgress = progress.currentBytes * 100 / progress.totalBytes
                        loadBar?.visibility = View.VISIBLE
                        loadBar?.progress = mProgress.toInt()
                    }
                    start(object : OnDownloadListener {
                        override fun onDownloadComplete() {
                            context.let {
                                val apk =
                                    File("${context.getExternalFilesDir("apk")?.path}/manager.apk")
                                val uri =
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                                        FileProvider.getUriForFile(
                                            context,
                                            "${context.packageName}.provider",
                                            apk
                                        )
                                    else
                                        Uri.fromFile(apk)

                                val intent = Intent(Intent.ACTION_VIEW)
                                intent.setDataAndType(uri, "application/vnd.android.package-archive")
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                context.startActivity(intent)
                            }
                        }

                        override fun onError(error: com.downloader.Error?) {
                            Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show()
                            Log.e("VMUpgrade", error.toString())
                        }

                    })
            }
        }
    }

}
