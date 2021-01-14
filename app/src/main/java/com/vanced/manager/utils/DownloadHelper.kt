package com.vanced.manager.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.content.FileProvider
import androidx.lifecycle.MutableLiveData
import com.github.kittinunf.fuel.Fuel
import com.vanced.manager.R
import com.vanced.manager.model.ProgressModel
import com.vanced.manager.utils.AppUtils.sendCloseDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

object DownloadHelper : CoroutineScope by CoroutineScope(Dispatchers.IO) {

    fun fuelDownload(url: String, fileFolder: String, fileName: String, context: Context, onDownloadComplete: () -> Unit, onError: (error: String) -> Unit) = launch {
        try {
            downloadProgress.value?.downloadingFile?.postValue(context.getString(R.string.downloading_file, fileName))
            downloadProgress.value?.currentDownload = Fuel.download(url)
                .fileDestination { _, _ ->
                    File(context.getExternalFilesDir(fileFolder)?.path, fileName)
                }
                .progress { readBytes, totalBytes ->
                    downloadProgress.value?.downloadProgress?.postValue((readBytes * 100 / totalBytes).toInt())
                }
                .responseString { _, _, result ->
                    result.fold(success = {
                        downloadProgress.value?.downloadProgress?.postValue(0)
                        onDownloadComplete()
                    }, failure = { error ->
                        downloadProgress.value?.downloadProgress?.postValue(0)
                        Log.d("VMDownloader", error.cause.toString())
                        onError(error.errorData.toString())
                    })
                }
        } catch (e: Exception) {
            downloadProgress.value?.downloadProgress?.postValue(0)
            Log.d("VMDownloader", "Failed to download file: $url")
            onError("")
        }

    }

    val downloadProgress = MutableLiveData<ProgressModel>()

    init {
        downloadProgress.value = ProgressModel()
    }

    fun downloadManager(context: Context) {
        val url = "https://github.com/YTVanced/VancedManager/releases/latest/download/manager.apk"
        fuelDownload(url, "manager", "manager.apk", context, onDownloadComplete = {
            val apk = File("${context.getExternalFilesDir("manager")?.path}/manager.apk")
            val uri =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    FileProvider.getUriForFile(context, "${context.packageName}.provider", apk)
                else
                    Uri.fromFile(apk)

            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(uri, "application/vnd.android.package-archive")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            context.startActivity(intent)
            sendCloseDialog(context)
        }, onError = {
            downloadProgress.value?.downloadingFile?.postValue(context.getString(R.string.error_downloading, "manager.apk"))
        })
    }

}
