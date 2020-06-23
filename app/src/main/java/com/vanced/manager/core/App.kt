package com.vanced.manager.core

import android.app.Application
import com.downloader.PRDownloader
import com.vanced.manager.utils.ThemeHelper.setFinalTheme

class App: Application() {

    override fun onCreate() {
        setFinalTheme(this)
        super.onCreate()
        PRDownloader.initialize(applicationContext)
    }

}