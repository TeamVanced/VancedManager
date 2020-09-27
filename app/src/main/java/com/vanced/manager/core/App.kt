package com.vanced.manager.core

import android.app.Application
import android.content.res.Configuration
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.beust.klaxon.JsonObject
import com.crowdin.platform.Crowdin
import com.crowdin.platform.CrowdinConfig
import com.crowdin.platform.data.remote.NetworkType
import com.downloader.PRDownloader
import com.vanced.manager.utils.InternetTools.baseUrl
import com.vanced.manager.utils.JsonHelper.getJson
import kotlinx.coroutines.*

open class App: Application() {

    var vanced: JsonObject? = null
    var music: JsonObject? = null
    var microg: JsonObject? = null
    var manager: JsonObject? = null

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

    fun loadJsonAsync() {
        val latest = runBlocking { getJson("${getDefaultSharedPreferences(this@App).getString("update_url", baseUrl)}/latest.json") }
        vanced = latest.obj("vanced")
        music = latest.obj("music")
        microg = latest.obj("microg")
        manager = latest.obj("manager")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Crowdin.onConfigurationChanged()
    }

}
