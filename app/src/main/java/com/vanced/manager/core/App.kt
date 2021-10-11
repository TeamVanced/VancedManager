package com.vanced.manager.core

import android.app.Application
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.topjohnwu.superuser.Shell
import com.vanced.manager.BuildConfig
import com.vanced.manager.utils.loadJson
import com.vanced.manager.utils.managerAccent
import com.vanced.manager.utils.mutableAccentColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class App : Application() {

    private val prefs by lazy { getDefaultSharedPreferences(this) }
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        scope.launch { loadJson(this@App) }
        super.onCreate()
        mutableAccentColor.value = prefs.managerAccent
        Shell.enableVerboseLogging = BuildConfig.DEBUG
        Shell.setDefaultBuilder(
            Shell.Builder
                .create()
                .setFlags(Shell.FLAG_REDIRECT_STDERR)
                //.setInitializers(BusyBoxInstaller::class.java) //TODO fix busybox
                .setTimeout(10)
        )
    }

}
