package com.vanced.manager.core

import android.app.Application
import android.content.res.Configuration
import android.util.Log
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.crowdin.platform.Crowdin
import com.crowdin.platform.CrowdinConfig
import com.crowdin.platform.data.model.AuthConfig
import com.crowdin.platform.data.remote.NetworkType
import com.vanced.manager.BuildConfig.*
import com.vanced.manager.utils.loadJson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

open class App: Application() {

    private val prefs by lazy { getDefaultSharedPreferences(this) }
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        scope.launch { loadJson(this@App) }
        super.onCreate()

        Crowdin.init(this,
            CrowdinConfig.Builder().apply {
                withDistributionHash(CROWDIN_HASH)
                withNetworkType(NetworkType.WIFI)
                if (ENABLE_CROWDIN_AUTH) {
                    if (prefs.getBoolean("crowdin_real_time", false))
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

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Crowdin.onConfigurationChanged()
    }
}
