package com.vanced.manager.core

import android.app.Application
import android.content.res.Configuration
import com.beust.klaxon.JsonObject
import com.crowdin.platform.Crowdin
import com.crowdin.platform.CrowdinConfig
import com.crowdin.platform.data.remote.NetworkType
import com.downloader.PRDownloader
import com.vanced.manager.utils.InternetTools.baseUrl
import com.vanced.manager.utils.JsonHelper.getJson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

open class App: Application() {

    var vanced: JsonObject? = null
    var music: JsonObject? = null
    var microg: JsonObject? = null
    var manager: JsonObject? = null

    override fun onCreate() {
        CoroutineScope(Dispatchers.IO).launch {
            loadJsonAsync().await()
            super.onCreate()
            PRDownloader.initialize(this@App)

            Crowdin.init(this@App,
                CrowdinConfig.Builder()
                    .withDistributionHash("36c51aed3180a4f43073d28j4s6")
                    .withNetworkType(NetworkType.WIFI)
                    .build()
            )
        }


    }

    fun loadJsonAsync() = CoroutineScope(Dispatchers.IO).async {
        val latest = getJson("$baseUrl/latest.json")
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
