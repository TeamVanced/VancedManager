package com.vanced.manager.ui.viewmodel

import android.app.Application
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vanced.manager.core.installer.util.uninstallPackage
import com.vanced.manager.core.preferences.holder.managerVariantPref
import com.vanced.manager.core.preferences.holder.musicEnabled
import com.vanced.manager.core.preferences.holder.vancedEnabled
import com.vanced.manager.domain.model.App
import com.vanced.manager.network.util.MICROG_NAME
import com.vanced.manager.network.util.MUSIC_NAME
import com.vanced.manager.network.util.VANCED_NAME
import com.vanced.manager.repository.JsonRepository
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: JsonRepository,
    private val app: Application
) : AndroidViewModel(app) {

    private val isNonroot
        get() = managerVariantPref == "nonroot"

    sealed class AppState {
        data class Fetching(val placeholderAppsCount: Int) : AppState()
        data class Success(val apps: List<App>) : AppState()
        data class Error(val error: String) : AppState()
    }

    var appState by mutableStateOf<AppState>(AppState.Fetching(3))
        private set

    fun fetch() {
        viewModelScope.launch {
            var appsCount = 0

            if (vancedEnabled) appsCount++
            if (musicEnabled) appsCount++
            if (isNonroot) appsCount++

            appState = AppState.Fetching(appsCount)

            try {
                with(repository.fetch()) {
                    val apps = mutableListOf<App>()

                    apps.apply {
                        if (vancedEnabled) add(vanced)
                        if (musicEnabled) add(music)
                        if (isNonroot) add(microg)
                    }

                    appState = AppState.Success(apps)
                }
            } catch (e: Exception) {
                val error = "failed to fetch: \n${e.stackTraceToString()}"
                appState = AppState.Error(error)
                Log.d(TAG, error)
            }
        }
    }

    fun launchApp(
        appName: String,
        appPackage: String,
        appPackageRoot: String?
    ) {
        val pkg = if (isNonroot) appPackage else appPackageRoot ?: appPackage
        val component = ComponentName(
            /* pkg = */ appPackage,
            /* cls = */ when (appName) {
                VANCED_NAME -> "$pkg.HomeActivity"
                MUSIC_NAME -> "$pkg.activities.MusicActivity"
                MICROG_NAME -> "org.microg.gms.ui.SettingsActivity"
                else -> throw IllegalArgumentException("$appName is not a valid app")
            }
        )

        try {
            app.startActivity(
                Intent().apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    setComponent(component)
                }
            )
        } catch (e: ActivityNotFoundException) {
            Log.d(TAG, "Unable to launch $appName")
            e.printStackTrace()
        }
    }

    //TODO implement root uninstallation
    fun uninstallApp(
        appPackage: String,
    ) {
        uninstallPackage(appPackage, app)
    }

    init {
        fetch()
    }

    companion object {

        const val TAG = "MainViewModel"

    }

}