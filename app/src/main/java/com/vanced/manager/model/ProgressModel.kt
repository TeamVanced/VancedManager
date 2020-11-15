package com.vanced.manager.model

import androidx.lifecycle.MutableLiveData

open class ProgressModel {

    val downloadProgress = MutableLiveData<Int>()
    val downloadingFile = MutableLiveData<String>()
    val installing = MutableLiveData<Boolean>()

    var currentDownload: Int = 0

    fun reset() {
        downloadProgress.value = 0
        downloadingFile.value = ""
    }

    init {
        installing.value = false
        reset()
    }
    
}
