package com.vanced.manager.core

import android.app.Application
import com.downloader.PRDownloader

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        PRDownloader.initialize(applicationContext)
    }

}