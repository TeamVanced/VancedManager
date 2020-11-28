package com.vanced.manager.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vanced.manager.feature.home.domain.entity.App
import com.vanced.manager.feature.home.domain.usecase.GetMicroGInformationUseCase
import com.vanced.manager.feature.home.domain.usecase.GetVancedManagerInformationUseCase
import com.vanced.manager.feature.home.domain.usecase.GetYouTubeMusicVancedInformationUseCase
import com.vanced.manager.feature.home.domain.usecase.GetYouTubeVancedInformationUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getMicroGInformationUseCase: GetMicroGInformationUseCase,
    private val getVancedManagerInformationUseCase: GetVancedManagerInformationUseCase,
    private val getYouTubeMusicVancedInformationUseCase: GetYouTubeMusicVancedInformationUseCase,
    private val getYouTubeVancedInformationUseCase: GetYouTubeVancedInformationUseCase
) : ViewModel() {

    private val _microG = MutableSharedFlow<App.MicroG>()
    val microG: SharedFlow<App.MicroG> =
        _microG.asSharedFlow()

    private val _vancedManager = MutableSharedFlow<App.VancedManager>()
    val vancedManager: SharedFlow<App.VancedManager> =
        _vancedManager.asSharedFlow()

    private val _youTubeVanced = MutableSharedFlow<App.YouTubeVanced>()
    val youTubeVanced: SharedFlow<App.YouTubeVanced> =
        _youTubeVanced.asSharedFlow()

    private val _youTubeVancedRoot = MutableSharedFlow<App.YouTubeVanced>()
    val youTubeVancedRoot: SharedFlow<App.YouTubeVanced> =
        _youTubeVancedRoot.asSharedFlow()

    private val _youTubeMusicVanced = MutableSharedFlow<App.YouTubeMusicVanced>()
    val youTubeMusicVanced: SharedFlow<App.YouTubeMusicVanced> =
        _youTubeMusicVanced.asSharedFlow()

    private val _youTubeMusicVancedRoot = MutableSharedFlow<App.YouTubeMusicVanced>()
    val youTubeMusicVancedRoot: SharedFlow<App.YouTubeMusicVanced> =
        _youTubeMusicVancedRoot.asSharedFlow()

    fun fetchAppInformation() {
        viewModelScope.launch {
            try {
                _microG.emit(getMicroGInformationUseCase { it.PKG_NAME })
                _vancedManager.emit(getVancedManagerInformationUseCase { it.PKG_NAME })
                _youTubeVanced.emit(getYouTubeVancedInformationUseCase { it.PKG_NAME })
                _youTubeVancedRoot.emit(getYouTubeVancedInformationUseCase { it.PKG_NAME_ROOT })
                _youTubeMusicVanced.emit(getYouTubeMusicVancedInformationUseCase { it.PKG_NAME })
                _youTubeMusicVancedRoot.emit(getYouTubeMusicVancedInformationUseCase { it.PKG_NAME_ROOT })
            } catch (exception: Exception) {
                //TODO state
            }
        }
    }
}