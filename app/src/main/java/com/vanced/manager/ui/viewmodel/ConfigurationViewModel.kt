package com.vanced.manager.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ConfigurationViewModel : ViewModel() {

    var currentIndex by mutableStateOf(0)
        private set

    fun next() {
        currentIndex++
    }

    fun back() {
        currentIndex--
    }

    fun reset() {
        currentIndex = 0
    }

}