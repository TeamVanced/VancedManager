package com.vanced.manager.utils

import androidx.lifecycle.MutableLiveData
import okhttp3.ResponseBody
import retrofit2.Call

val downloadProgress = MutableLiveData(0)
val downloadingFile = MutableLiveData("")
val installing = MutableLiveData(false)

var currentDownload: Call<ResponseBody>? = null

fun reset() {
    downloadProgress.value = 0
    downloadingFile.value = ""
}

fun postReset() {
    downloadProgress.postValue(0)
    downloadingFile.postValue("")
}

