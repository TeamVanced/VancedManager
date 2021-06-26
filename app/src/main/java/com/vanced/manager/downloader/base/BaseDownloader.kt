package com.vanced.manager.downloader.base

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.vanced.manager.installer.AppInstaller
import com.vanced.manager.util.log
import okhttp3.ResponseBody
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.Call
import retrofit2.awaitResponse
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

abstract class BaseDownloader(
    val appName: String
) : KoinComponent {

    var downloadProgress by mutableStateOf(0f)
        private set

    var downloadFile by mutableStateOf("")
        private set

    var installing by mutableStateOf(false)

    private lateinit var call: Call<ResponseBody>

    private val tag = this::class.simpleName!!

    val context: Context by inject()
    val appInstaller: AppInstaller by inject()

    abstract suspend fun download()

    suspend fun downloadFile(
        call: Call<ResponseBody>,
        folderStructure: String,
        fileName: String,
        onError: (error: String) -> Unit = {},
        onComplete: suspend () -> Unit = {},
    ) {
        fun error(errorBody: String) {
            downloadFile = "Error downloading $fileName"
            onError(errorBody)
            log(tag, errorBody)
        }
        try {
            this.call = call
            val response = call.awaitResponse()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    if (writeFile(body, fileName, folderStructure)) {
                        onComplete()
                    }
                }
            } else {
                val error = response.errorBody()?.toString()
                if (error != null) {
                    error(error)
                }
            }
        } catch (e: Exception) {
            error(e.stackTraceToString())
        }
        downloadProgress = 0f
    }

    fun cancelDownload() {
        call.cancel()
    }

    private fun writeFile(
        body: ResponseBody,
        fileName: String,
        folderStructure: String
    ): Boolean {
        val file = File("${context.getExternalFilesDir(appName)?.path}/$folderStructure/$fileName")
        val inputStream = body.byteStream()
        val outputStream = FileOutputStream(file)
        return try {
            val totalBytes = body.contentLength()
            val fileReader = ByteArray(4096)
            var downloadedBytes = 0L
            var read: Int
            while (inputStream.read(fileReader).also { read = it } != -1) {
                outputStream.write(fileReader, 0, read)
                downloadedBytes += read
                downloadProgress = (downloadedBytes * 100 / totalBytes).toFloat()
            }
            true
        } catch (e: IOException) {
            false
        } finally {
            inputStream.close()
            outputStream.close()
        }
    }



}