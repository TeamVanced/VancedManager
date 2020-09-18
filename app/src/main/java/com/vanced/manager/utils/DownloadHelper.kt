package com.vanced.manager.utils

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import com.vanced.manager.R

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

}
