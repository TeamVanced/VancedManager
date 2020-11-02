package com.vanced.manager.core

import android.app.Application
import android.content.res.Configuration
import androidx.databinding.ObservableField
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.beust.klaxon.JsonObject
import com.crowdin.platform.Crowdin
import com.crowdin.platform.CrowdinConfig
import com.crowdin.platform.data.model.AuthConfig
import com.crowdin.platform.data.remote.NetworkType
import com.downloader.PRDownloader
import com.vanced.manager.utils.InternetTools.baseUrl
import com.vanced.manager.utils.JsonHelper.getJson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

open class App: Application() {

    var vanced = ObservableField<JsonObject?>()
    var music = ObservableField<JsonObject?>()
    var microg = ObservableField<JsonObject?>()
    var manager = ObservableField<JsonObject?>()

    private val prefs by lazy { getDefaultSharedPreferences(this) }

    //var braveTiers = ObservableField<JsonObject?>()

    override fun onCreate() {
        loadJson()
        super.onCreate()
        PRDownloader.initialize(this)
        val clientId = prefs.getString("crowdin_client_id", "")
        val clientSecret = prefs.getString("crowdin_client_secret", "")

        Crowdin.init(this,
            CrowdinConfig.Builder().apply {
                withDistributionHash("3b84be9663023b0b1a22988j4s6")
                withNetworkType(NetworkType.WIFI)
                if (clientId != "" && clientSecret != "") {
                    withRealTimeUpdates()
                    withSourceLanguage("en")
                    withAuthConfig(AuthConfig(clientId!!, clientSecret!!, null))
                    withScreenshotEnabled()
                }
            }.build()
        )
        if (prefs.getBoolean("crowdin_upload_screenshot", false))
            Crowdin.registerScreenShotContentObserver(this)

    }

    open fun loadJson() = CoroutineScope(Dispatchers.IO).launch {
        val installUrl = prefs.getString("install_url", baseUrl)
        val latest = getJson("$installUrl/latest.json")
//        braveTiers.apply {
//            set(getJson("$installUrl/sponsor.json"))
//            notifyChange()
//        }

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
