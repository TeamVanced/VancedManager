package com.vanced.manager.core

import android.app.Application
import android.content.res.Configuration
import com.beust.klaxon.JsonObject
import com.crowdin.platform.Crowdin
import com.crowdin.platform.CrowdinConfig
import com.crowdin.platform.data.remote.NetworkType
import com.downloader.PRDownloader
import com.vanced.manager.utils.JsonHelper.getJson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

open class App: Application() {

    lateinit var vanced : JsonObject
    lateinit var music: JsonObject
    lateinit var microg: JsonObject
    lateinit var manager: JsonObject

    override fun onCreate() {
        loadJson()
        super.onCreate()
        PRDownloader.initialize(this)

        //if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            Crowdin.init(this,
            CrowdinConfig.Builder()
                .withDistributionHash("36c51aed3180a4f43073d28j4s6")
                .withNetworkType(NetworkType.WIFI)
                .build()
            )
        //}

    }

    fun loadJson() = CoroutineScope(Dispatchers.IO).launch {
        val latest = getJson("https://vanced.app/api/v1/latest.json")
        vanced = latest.obj("vanced")!!
        music = latest.obj("music")!!
        microg = latest.obj("microg")!!
        manager = latest.obj("manager")!!
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Crowdin.onConfigurationChanged()
    }

}
