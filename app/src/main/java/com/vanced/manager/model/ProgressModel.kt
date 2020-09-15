package com.vanced.manager.model

open class ProgressModel {
    
    private var downloadProgress = 0
    private var downloadingFile = ""
    
    var showInstallCircle = false
    var showDownloadBar = false
    
    open fun getDownloadProgress(): Int {
        return downloadProgress
    }
    
    open fun setDownloadProgress(progress: Int) {
        downloadProgress = progress
    }
    
    open fun getDownloadingFile(): String {
        return downloadingFile
    }
    
    open fun setDownloadingFile(file: String) {
        downloadingFile = file
    }
    
}
