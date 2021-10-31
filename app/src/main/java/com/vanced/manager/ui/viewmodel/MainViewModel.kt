package com.vanced.manager.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vanced.manager.domain.model.App
import com.vanced.manager.core.preferences.holder.managerVariantPref
import com.vanced.manager.core.preferences.holder.musicEnabled
import com.vanced.manager.core.preferences.holder.vancedEnabled
import com.vanced.manager.repository.JsonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: JsonRepository
) : ViewModel() {

    sealed class AppState {

        data class Fetching(val placeholderAppsCount: Int) : AppState()

        data class Success(val apps: List<App>) : AppState()

        data class Error(val error: String) : AppState()

    }

    private val _appState = MutableStateFlow<AppState>(AppState.Fetching(3))
    val appState: StateFlow<AppState> = _appState

    fun fetch() {
        viewModelScope.launch(Dispatchers.IO) {
            val vancedEnabled = vancedEnabled.value.value
            val musicEnabled = musicEnabled.value.value
            val isNonroot = managerVariantPref.value.value == "nonroot"

            var appsCount = 0

            if (vancedEnabled) appsCount++
            if (musicEnabled) appsCount++
            if (isNonroot) appsCount++

            _appState.value = AppState.Fetching(appsCount)

            try {
                with(repository.fetch()) {
                    val apps = mutableListOf<App>()

                    apps.apply {
                        if (vancedEnabled) add(vanced)
                        if (musicEnabled) add(music)
                        if (isNonroot) add(microg)
                    }

                    _appState.value = AppState.Success(apps)
                }
            } catch (e: Exception) {
                val error = "failed to fetch: \n${e.stackTraceToString()}"
                _appState.value = AppState.Error(error)
                Log.d("MainViewModel", error)
            }
        }
    }

    init {
        fetch()
    }

}