package com.vanced.manager.core

import android.app.Application
import android.content.res.Configuration
import android.util.Log
import androidx.databinding.ObservableField
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.beust.klaxon.JsonObject
import com.crowdin.platform.Crowdin
import com.crowdin.platform.CrowdinConfig
import com.crowdin.platform.data.model.AuthConfig
import com.crowdin.platform.data.remote.NetworkType
import com.downloader.PRDownloader
import com.vanced.manager.BuildConfig.*
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

        Crowdin.init(this,
            CrowdinConfig.Builder().apply {
                withDistributionHash(CROWDIN_HASH)
                withNetworkType(NetworkType.WIFI)
                if (ENABLE_CROWDIN_AUTH) {
                    withRealTimeUpdates()
                    withSourceLanguage("en")
                    withAuthConfig(AuthConfig(CROWDIN_CLIENT_ID, CROWDIN_CLIENT_SECRET, null))
                    withScreenshotEnabled()
                    Log.d("test", "crowdin credentials")
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
