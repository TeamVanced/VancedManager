package com.vanced.manager.core

import android.app.Application
import android.content.res.Configuration
import androidx.databinding.ObservableField
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.beust.klaxon.JsonObject
import com.crowdin.platform.Crowdin
import com.crowdin.platform.CrowdinConfig
import com.crowdin.platform.data.remote.NetworkType
import com.downloader.PRDownloader
import com.vanced.manager.utils.InternetTools.baseUrl
import com.vanced.manager.utils.JsonHelper.getJson
import kotlinx.coroutines.runBlocking

open class App: Application() {

    var vanced = ObservableField<JsonObject?>()
    var music = ObservableField<JsonObject?>()
    var microg = ObservableField<JsonObject?>()
    var manager = ObservableField<JsonObject?>()

    override fun onCreate() {
        loadJsonAsync()
        super.onCreate()
        PRDownloader.initialize(this)

        Crowdin.init(this,
            CrowdinConfig.Builder()
                .withDistributionHash("36c51aed3180a4f43073d28j4s6")
                .withNetworkType(NetworkType.WIFI)
                .build()
        )

    }

    open fun loadJsonAsync() {
        val latest = runBlocking { getJson("${getDefaultSharedPreferences(this@App).getString("install_url", baseUrl)}/latest.json") }

        vanced.set(latest?.obj("vanced"))
        music.set(latest?.obj("music"))
        microg.set(latest?.obj("microg"))
        manager.set(latest?.obj("manager"))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Crowdin.onConfigurationChanged()
    }


}
