package com.vanced.manager.core

import android.app.Application
import com.downloader.PRDownloader
import com.vanced.manager.utils.NotificationHelper.createNotifChannel

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        PRDownloader.initialize(this)
        createNotifChannel(this)
    }

}