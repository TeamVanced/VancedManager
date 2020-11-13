package com.vanced.manager.model

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt

open class ProgressModel {
    
    val downloadProgress = ObservableInt()
    val downloadingFile = ObservableField<String>()
    val installing = ObservableBoolean()

    var currentDownload: Int = 0

    fun reset() {
        downloadProgress.set(0)
        downloadingFile.set("")
    }

    init {
        installing.set(false)
        reset()
    }
    
}
