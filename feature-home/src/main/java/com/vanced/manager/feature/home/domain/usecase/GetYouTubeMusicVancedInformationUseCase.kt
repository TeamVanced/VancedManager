package com.vanced.manager.feature.home.domain.usecase

import com.vanced.manager.feature.home.domain.entity.App
import com.vanced.manager.feature.home.domain.repository.AppRepository

class GetYouTubeMusicVancedInformationUseCase(
    private val repository: AppRepository
) {

    suspend operator fun invoke(
        packageName: (App.YouTubeMusicVanced.Companion) -> String
    ): App.YouTubeMusicVanced = repository.getYouTubeMusicVancedInformation(packageName)
}