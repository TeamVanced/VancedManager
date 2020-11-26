package com.vanced.manager.feature.home.presentation

import androidx.lifecycle.ViewModel
import com.vanced.manager.feature.home.domain.usecase.*

class HomeViewModel(
    private val getAppInformationUseCase: GetAppInformationUseCase,
    private val getMicroGInformationUseCase: GetMicroGInformationUseCase,
    private val getVancedManagerInformationUseCase: GetVancedManagerInformationUseCase,
    private val getYouTubeVancedInformationUseCase: GetYouTubeVancedInformationUseCase,
    private val getYouTubeMusicVancedInformationUseCase: GetYouTubeMusicVancedInformationUseCase
) : ViewModel()