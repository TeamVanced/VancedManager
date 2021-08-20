package com.vanced.manager.downloader.base

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.vanced.manager.domain.model.App
import com.vanced.manager.installer.base.AppInstaller
import com.vanced.manager.ui.viewmodel.MainViewModel
import com.vanced.manager.util.log
import okhttp3.ResponseBody
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.Call
import retrofit2.awaitResponse
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

//TODO remove showDownloadScreen and viewModel references
abstract class AppDownloader(
    val appName: String,
    val appInstaller: AppInstaller
) : KoinComponent {

    data class File(
        val call: Call<ResponseBody>,
        val fileName: String
    )

    var downloadProgress by mutableStateOf(0f)
        private set

    var downloadFile by mutableStateOf("")
        private set

    var installing by mutableStateOf(false)
    var error by mutableStateOf(false)

    val showDownloadScreen = mutableStateOf(false)

    private var canceled = false

    private lateinit var call: Call<ResponseBody>

    private val tag = this::class.simpleName!!

    private fun resetValues() {
        showDownloadScreen.value = false
        downloadProgress = 0f
        downloadFile = ""
        installing = false
        error = false
        canceled = false
    }

    val context: Context by inject()

    abstract suspend fun download(
        app: App,
        viewModel: MainViewModel
    )

    private fun install(viewModel: MainViewModel) {
        installing = true
        appInstaller.install {
            viewModel.fetch()
            resetValues()
        }
    }

    suspend fun downloadFile(
        file: File,
        viewModel: MainViewModel,
        folderStructure: String,
        onError: (error: String) -> Unit = {},
    ) {
        downloadFiles(
            files = listOf(file),
            viewModel = viewModel,
            folderStructure = folderStructure,
            onError = onError
        )
    }

    suspend fun downloadFiles(
        files: List<File>,
        viewModel: MainViewModel,
        folderStructure: String,
        onError: (error: String) -> Unit = {},
    ) {
        showDownloadScreen.value = true
        fun error(errorBody: String) {
            error = true
            onError(errorBody)
            log(tag, errorBody)
        }
        try {
            for (file in files) {
                if (canceled) {
                    break
                }

                val fileName = file.fileName
                downloadFile = fileName
                this.call = file.call
                val response = call.awaitResponse()
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        if (!writeFile(body, fileName, folderStructure)) {
                            error("Failed to write file data")
                        }
                    }
                } else {
                    val error = response.errorBody()?.toString()
                    if (error != null) {
                        error(error)
                    }
                }
            }
        } catch (e: Exception) {
            error(e.stackTraceToString())
        }

        if (canceled) {
            resetValues()
            return
        }

        install(viewModel)
    }

    fun cancelDownload() {
        canceled = true
        call.cancel()
    }

    private fun writeFile(
        body: ResponseBody,
        fileName: String,
        folderStructure: String
    ): Boolean {
        val folder = File("${context.getExternalFilesDir(appName)?.path}/$folderStructure")
        folder.mkdirs()
        val file = File("${folder.path}/$fileName")
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