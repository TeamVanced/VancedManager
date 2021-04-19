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
                        if (response.body()?.let {
                                writeFile(
                                    it,
                                    context.getExternalFilesDir(fileFolder)?.path + "/" + fileName
                                )
                            } == true) {
                            onDownloadComplete()
                        } else {
                            onError("Could not save file")
                            downloadProgress.postValue(0)
                            log(
                                "VMDownloader",
                                "Failed to save file: $url\n${response.errorBody()}"
                            )
                        }
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
                    downloadProgress.postValue(0)
                } else {
                    onError(t.stackTraceToString())
                    downloadProgress.postValue(0)
                    log("VMDownloader", "Failed to download file: $url")
                }
            }

        })
    }

    fun writeFile(body: ResponseBody, filePath: String): Boolean {
        return try {
            val file = File(filePath)
            val totalBytes = body.contentLength()
            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null
            try {
                val fileReader = ByteArray(4096)
                var downloadedBytes: Long = 0
                inputStream = body.byteStream()
                outputStream = FileOutputStream(file)
                var read: Int
                while (inputStream.read(fileReader).also { read = it } != -1) {
                    outputStream.write(fileReader, 0, read)
                    downloadedBytes += read.toLong()
                    downloadProgress.postValue((downloadedBytes * 100 / totalBytes).toInt())
                }
                outputStream.flush()
                true
            } catch (e: IOException) {
                false
            } finally {
                inputStream?.close()
                outputStream?.close()
            }
        } catch (e: IOException) {
            false
        }
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
                try {
                    context.startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    log("VMDownloader", e.stackTraceToString())
                } finally {
                    sendCloseDialog(context)
                }
            },
            onError = {
                downloadingFile.postValue(
                    context.getString(
                        R.string.error_downloading,
                        "manager.apk"
                    )
                )
            })
    }

}
