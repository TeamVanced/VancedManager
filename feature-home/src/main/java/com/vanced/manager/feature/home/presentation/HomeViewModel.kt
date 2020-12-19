package com.vanced.manager.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vanced.manager.feature.home.domain.entity.AppInfo
import com.vanced.manager.feature.home.domain.usecase.GetAppsInfoUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class HomeViewModel(
    private val getAppsInfoUseCase: GetAppsInfoUseCase
) : ViewModel() {

    private val _appsList = MutableSharedFlow<List<AppInfo>>()
    val appsList: SharedFlow<List<AppInfo>> =
        _appsList.asSharedFlow()

    fun fetchAppsInfo() {
        viewModelScope.launch {
            try {
                _appsList.emit(getAppsInfoUseCase())
            } catch (exception: HttpException) {
                //TODO state
            }
        }
    }
}