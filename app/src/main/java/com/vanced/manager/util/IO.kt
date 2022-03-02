package com.vanced.manager.util

import okhttp3.ResponseBody
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

inline fun ResponseBody.writeFile(
    filePath: String,
    onProgress: (Float) -> Unit
) {
    byteStream().use { inputStream ->
        FileOutputStream(filePath).use { outputStream ->
            val totalBytes = contentLength()
            inputStream.copyTo(outputStream, 8192) { bytes ->
                onProgress((bytes * 100 / totalBytes).toFloat())
            }
        }
    }
}

inline fun InputStream.copyTo(
    outputStream: OutputStream,
    bufferSize: Int,
    onProgress: (Long) -> Unit
) {
    val buffer = ByteArray(bufferSize)
    var bytesCopied: Long = 0
    var bytes = read(buffer)
    while (bytes >= 0) {
        outputStream.write(buffer, 0, bytes)
        bytesCopied += bytes
        bytes = read(buffer)
        onProgress(bytesCopied)
    }
}