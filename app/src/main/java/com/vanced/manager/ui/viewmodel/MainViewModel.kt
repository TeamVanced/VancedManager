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
import com.vanced.manager.domain.model.App
import com.vanced.manager.network.util.MICROG_NAME
import com.vanced.manager.network.util.MUSIC_NAME
import com.vanced.manager.network.util.VANCED_NAME
import com.vanced.manager.repository.MainRepository
import com.vanced.manager.repository.MirrorRepository
import kotlinx.coroutines.launch

class MainViewModel(
    private val mainRepository: MainRepository,
    private val mirrorRepository: MirrorRepository,
    private val app: Application
) : AndroidViewModel(app) {

    private val isRoot
        get() = managerVariantPref == "root"

    private val appCount: Int
        get() = if (isRoot) 2 else 3

    sealed class AppState {
        data class Fetching(val placeholderAppsCount: Int) : AppState()
        data class Success(val apps: List<App>) : AppState()
        data class Error(val error: String) : AppState()
    }

    var appState by mutableStateOf<AppState>(AppState.Fetching(appCount))
        private set

    fun fetch() {
        viewModelScope.launch {
            appState = AppState.Fetching(appCount)

            fetchData()
        }
    }

    fun launchApp(
        appName: String,
        appPackage: String,
    ) {
        val component = ComponentName(
            /* pkg = */ appPackage,
            /* cls = */ when (appName) {
                VANCED_NAME -> "com.google.android.youtube.HomeActivity"
                MUSIC_NAME -> "com.google.android.apps.youtube.music.activities.MusicActivity"
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

    private suspend fun fetchData(
        fromMirror: Boolean = false
    ) {
        try {
            val repository = if (fromMirror) mirrorRepository else mainRepository
            with(repository.fetch()) {
                appState = AppState.Success(apps)
            }
        } catch (e: Exception) {
            if (!fromMirror) {
                fetchData(true)
                return
            }

            val error = "failed to fetch: \n${e.stackTraceToString()}"
            appState = AppState.Error(error)
            Log.d(TAG, error)
        }
    }

    companion object {
        const val TAG = "MainViewModel"
    }

}