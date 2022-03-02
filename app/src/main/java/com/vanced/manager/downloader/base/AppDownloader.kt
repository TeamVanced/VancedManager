package com.vanced.manager.downloader.base

import com.vanced.manager.util.writeFile
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.awaitResponse

abstract class AppDownloader {

    data class DownloadFile(
        val fileName: String,
        val call: Call<ResponseBody>,
    )

    sealed class DownloadStatus {
        object Success : DownloadStatus()
        data class Error(val error: String, val fileName: String) : DownloadStatus()

        val isSuccess
            get() = this is Success

        val isError
            get() = this is Error
    }

    abstract suspend fun download(
        appVersions: List<String>?,
        onProgress: (Float) -> Unit,
        onFile: (String) -> Unit
    ): DownloadStatus

    abstract suspend fun downloadRoot(
        appVersions: List<String>?,
        onProgress: (Float) -> Unit,
        onFile: (String) -> Unit
    ): DownloadStatus

    abstract fun getSavedFilePath(): String

    suspend inline fun downloadFiles(
        files: Array<DownloadFile>,
        onProgress: (Float) -> Unit,
        onFile: (String) -> Unit
    ): DownloadStatus {
        for (file in files) {
            try {
                onFile(file.fileName)

                val response = file.call.awaitResponse()
                if (response.isSuccessful) {
                    response.body()?.writeFile(getSavedFilePath() + "/${file.fileName}", onProgress)
                    continue
                }

                val error = response.errorBody()?.toString()
                if (error != null) {
                    return DownloadStatus.Error(error, file.fileName)
                }
            } catch (e: Exception) {
                return DownloadStatus.Error(e.stackTraceToString(), file.fileName)
            }
        }

        return DownloadStatus.Success
    }

}