package com.vanced.manager.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import com.vanced.manager.R
import com.vanced.manager.library.network.providers.createService
import com.vanced.manager.utils.AppUtils.log
import com.vanced.manager.utils.AppUtils.sendCloseDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url
import java.io.*

object DownloadHelper : CoroutineScope by CoroutineScope(Dispatchers.IO) {

    interface DownloadHelper {

        @Streaming
        @GET
        fun download(@Url url: String): Call<ResponseBody>

    }

    fun download(
        url: String,
        baseUrl: String,
        fileFolder: String,
        fileName: String,
        context: Context,
        onDownloadComplete: () -> Unit = {},
        onError: (error: String) -> Unit = {}
    ) {
        downloadingFile.postValue(context.getString(R.string.downloading_file, fileName))
        val downloadInterface = createService(DownloadHelper::class, baseUrl)
        val download = downloadInterface.download(url)
        currentDownload = download
        download.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    launch {
                        if (response.body()?.let { writeFile(it, fileFolder, fileName) } == true) {
                            onDownloadComplete()
                        } else {
                            onError(response.errorBody().toString())
                            log("VMDownloader", "Failed to save file: $url\n${response.errorBody()}")
                        }
                        downloadProgress.postValue(0)
                    }
                } else {
                    val errorBody = response.errorBody().toString()
                    onError(errorBody)
                    downloadProgress.postValue(0)
                    log("VMDownloader", "Failed to download file: $url\n$errorBody")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                if (call.isCanceled) {
                    log("VMDownloader", "Download canceled")
                } else {
                    onError(t.stackTraceToString())
                    log("VMDownloader", "Failed to download file: $url")
                }
                downloadProgress.postValue(0)
            }

        })
    }

    fun writeFile(body: ResponseBody, folderName: String, fileName: String): Boolean =
        try {
            val totalBytes = body.contentLength()
            val fileReader = ByteArray(8096)
            var downloadedBytes: Long = 0
            val dir = File(managerPath, folderName).apply {
                if (!exists()) {
                    mkdirs()
                }
            }
            val inputStream: InputStream = body.byteStream()
            val outputStream: OutputStream = FileOutputStream("${dir.path}/$fileName")
            var read: Int
            while (inputStream.read(fileReader).also { read = it } != -1) {
                outputStream.write(fileReader, 0, read)
                downloadedBytes += read.toLong()
                downloadProgress.postValue((downloadedBytes * 100 / totalBytes).toInt())
            }
            outputStream.flush()
            inputStream.close()
            outputStream.close()
            true
        } catch (e: IOException) {
            log("VMIO", "Failed to save file: $e")
            false
        }

    fun downloadManager(context: Context) {
        val url = "https://github.com/YTVanced/VancedManager/releases/latest/download/manager.apk"
        download(
            url,
            "https://github.com/YTVanced/VancedManager/",
            "manager",
            "manager.apk",
            context,
            onDownloadComplete = {
                val apk = File("manager/manager.apk".managerFilepath)
                val uri =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                        FileProvider.getUriForFile(context, "${context.packageName}.provider", apk)
                    else
                        Uri.fromFile(apk)

                val intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(uri, "application/vnd.android.package-archive")
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                try {
                    context.startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    log("VMDownloader", e.stackTraceToString())
                } finally {
                    sendCloseDialog(context)
                }
            }) {
            downloadingFile.postValue(
                context.getString(
                    R.string.error_downloading,
                    "manager.apk"
                )
            )
        }
    }

}
