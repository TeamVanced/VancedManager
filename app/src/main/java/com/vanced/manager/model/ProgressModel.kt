package com.vanced.manager.model

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt

open class ProgressModel {
    
    val downloadProgress = ObservableInt()
    val downloadingFile = ObservableField<String>()
    val showInstallCircle = ObservableBoolean()
    val showDownloadBar = ObservableBoolean()

    var currentDownload: Int = 0

    init {
        downloadProgress.set(0)
        downloadingFile.set("")
        showInstallCircle.set(false)
        showDownloadBar.set(false)
    }
    
}
