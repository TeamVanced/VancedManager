package com.vanced.manager.core.downloader.base

import com.vanced.manager.core.downloader.util.DownloadStatus
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.awaitResponse
import java.io.FileOutputStream

typealias DownloadCall = Call<ResponseBody>

abstract class AppDownloader {

    data class DownloadFile(
        val fileName: String,
        val call: DownloadCall,
    )

    private lateinit var call: DownloadCall

    abstract suspend fun download(
        appVersions: List<String>?,
        onStatus: (DownloadStatus) -> Unit
    )

    abstract fun getSavedFilePath(): String

    suspend fun downloadFiles(
        downloadFiles: Array<DownloadFile>,
        onFile: (String) -> Unit,
        onProgress: (Float) -> Unit,
        onError: (error: String, fileName: String) -> Unit,
        onSuccess: () -> Unit
    ) {
        for (downloadFile in downloadFiles) {
            try {
                this.call = downloadFile.call

                onFile(downloadFile.fileName)

                val response = call.awaitResponse()
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        writeFile(body, downloadFile.fileName, onProgress)
                    }
                    continue
                }

                val error = response.errorBody()?.toString()
                if (error != null) {
                    onError(error, downloadFile.fileName)
                    return
                }
            } catch (e: Exception) {
                onError(e.stackTraceToString(), downloadFile.fileName)
                return
            }
        }

        onSuccess()
    }

    private inline fun writeFile(
        body: ResponseBody,
        fileName: String,
        onProgress: (Float) -> Unit
    ) {
        val inputStream = body.byteStream()
        val outputStream = FileOutputStream(getSavedFilePath() + "/$fileName")
        val totalBytes = body.contentLength()
        val fileReader = ByteArray(4096)
        var downloadedBytes = 0L
        var read: Int
        while (inputStream.read(fileReader).also { read = it } != -1) {
            outputStream.write(fileReader, 0, read)
            downloadedBytes += read
            onProgress((downloadedBytes * 100 / totalBytes).toFloat())
        }
        inputStream.close()
        outputStream.close()
    }

}