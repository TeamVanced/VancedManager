package com.vanced.manager.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.vanced.manager.domain.model.App
import com.vanced.manager.repository.JsonRepository
import kotlinx.coroutines.launch

class HomeViewModel(
    context: Context,
    private val repository: JsonRepository
) : ViewModel() {

    private val vanced = mutableStateOf(App())
    private val music = mutableStateOf(App())
    private val microg = mutableStateOf(App())
    private var manager = mutableStateOf(App())

    var isFetching by mutableStateOf(false)

    val apps = mutableStateListOf<MutableState<App>>()

    fun fetch() {
        viewModelScope.launch {
            isFetching = true
            try {
                with(repository.fetch()) {
                    this@HomeViewModel.vanced.value = vanced
                    this@HomeViewModel.music.value = music
                    this@HomeViewModel.microg.value = microg
                }
            } catch (e: Exception) {
                Log.d("HomeViewModel", "failed to fetch: $e")
            }

            isFetching = false
        }
    }

    init {

        val prefs = getDefaultSharedPreferences(context)
        val variant = prefs.getString("manager_variant", "nonroot")
        val vancedEnabled = prefs.getBoolean("manager_vanced_enabled", true)
        val musicEnabled = prefs.getBoolean("manager_music_enabled", true)

        apps.apply {
            if (vancedEnabled) {
                add(vanced)
            }

            if (musicEnabled) {
                add(music)
            }

            if (variant == "nonroot") {
                add(microg)
            }

        }

        fetch()
    }

}