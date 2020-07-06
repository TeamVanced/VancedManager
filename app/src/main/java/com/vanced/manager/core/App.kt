package com.vanced.manager.core

import android.app.Application
import android.content.res.Configuration
import com.crowdin.platform.Crowdin
import com.crowdin.platform.CrowdinConfig
import com.crowdin.platform.data.remote.NetworkType
import com.downloader.PRDownloader
import com.vanced.manager.utils.NotificationHelper.createNotifChannel

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        PRDownloader.initialize(this)
        createNotifChannel(this)

        Crowdin.init(this,
        CrowdinConfig.Builder()
            .withDistributionHash("36c51aed3180a4f43073d28j4s6")
            .withNetworkType(NetworkType.WIFI)
            .build())
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Crowdin.onConfigurationChanged()
    }

}