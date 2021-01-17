package com.vanced.manager.model

import androidx.lifecycle.MutableLiveData
import okhttp3.ResponseBody
import retrofit2.Call

open class ProgressModel {

    val downloadProgress = MutableLiveData<Int>()
    val downloadingFile = MutableLiveData<String>()
    val installing = MutableLiveData<Boolean>()

    var currentDownload: Call<ResponseBody>? = null

    fun reset() {
        downloadProgress.value = 0
        downloadingFile.value = ""
    }

    fun postReset() {
        downloadProgress.postValue(0)
        downloadingFile.postValue("")
    }

    init {
        installing.postValue(false)
        reset()
    }
    
}
