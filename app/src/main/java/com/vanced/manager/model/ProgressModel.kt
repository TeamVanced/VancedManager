package com.vanced.manager.model

open class ProgressModel {
    
    private var downloadProgres = 0
    private var downloadingFile = ""
    
    var showInstallCircle = false
    var showDownloadBar = false
    
    open fun getDownloadProgress(): Int {
        return downloadProgress
    }
    
    open fun setDownloadProgress(progresss: Int) {
        downloadProgress = progress
    }
    
    open fun getDownloadingFile(): String {
        return downloadProgress
    }
    
    open fun setDownloadingFile(file: String) {
        downloadingFile = file
    }
    
}
