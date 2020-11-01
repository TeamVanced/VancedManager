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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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

    open fun loadJsonAsync() = CoroutineScope(Dispatchers.IO).launch {
        val latest = getJson("${getDefaultSharedPreferences(this@App).getString("install_url", baseUrl)}/latest.json")

        vanced.apply {
            set(latest?.obj("vanced"))
            notifyChange()
        }
        music.apply {
            set(latest?.obj("music"))
            notifyChange()
        }
        microg.apply {
            set(latest?.obj("microg"))
            notifyChange()
        }
        manager.apply {
            set(latest?.obj("manager"))
            notifyChange()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Crowdin.onConfigurationChanged()
    }


}
