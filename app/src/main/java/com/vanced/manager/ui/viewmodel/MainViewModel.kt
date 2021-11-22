package com.vanced.manager.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vanced.manager.core.preferences.holder.managerVariantPref
import com.vanced.manager.core.preferences.holder.musicEnabled
import com.vanced.manager.core.preferences.holder.vancedEnabled
import com.vanced.manager.domain.model.App
import com.vanced.manager.repository.JsonRepository
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: JsonRepository
) : ViewModel() {

    sealed class AppState {
        data class Fetching(val placeholderAppsCount: Int) : AppState()
        data class Success(val apps: List<App>) : AppState()
        data class Error(val error: String) : AppState()
    }

    var appState by mutableStateOf<AppState>(AppState.Fetching(3))
        private set

    fun fetch() {
        viewModelScope.launch {
            val isNonroot = managerVariantPref == "nonroot"

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
                Log.d("MainViewModel", error)
            }
        }
    }

    init {
        fetch()
    }

}