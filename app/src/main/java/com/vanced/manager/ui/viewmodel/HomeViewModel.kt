package com.vanced.manager.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vanced.manager.domain.model.App
import com.vanced.manager.preferences.holder.managerVariantPref
import com.vanced.manager.preferences.holder.musicEnabled
import com.vanced.manager.preferences.holder.vancedEnabled
import com.vanced.manager.repository.JsonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    context: Context,
    private val repository: JsonRepository
) : ViewModel() {

    private val vanced = MutableStateFlow(App())
    private val music = MutableStateFlow(App())
    private val microg = MutableStateFlow(App())
    private var manager = MutableStateFlow(App())

    private val _isFetching = MutableStateFlow(false)
    val isFetching: StateFlow<Boolean> = _isFetching

    val apps = mutableListOf<StateFlow<App>>()

    fun fetch() {
        viewModelScope.launch(Dispatchers.IO) {
            _isFetching.value = true
            try {
                with(repository.fetch()) {
                    this@HomeViewModel.vanced.value = vanced
                    this@HomeViewModel.music.value = music
                    this@HomeViewModel.microg.value = microg
                }
            } catch (e: Exception) {
                Log.d("HomeViewModel", "failed to fetch: $e")
            }

            _isFetching.value = false
        }
    }

    init {
        apps.apply {
            if (vancedEnabled.value.value) add(vanced)
            if (musicEnabled.value.value) add(music)
            if (managerVariantPref.value.value == "nonroot") add(microg)
        }

        fetch()
    }

}