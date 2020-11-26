package com.vanced.manager.feature.home.domain.usecase

import com.vanced.manager.feature.home.domain.entity.YouTubeMusicVanced
import com.vanced.manager.feature.home.domain.repository.AppInformationRepository

class GetYouTubeMusicVancedInformationUseCase(
    private val repository: AppInformationRepository
) {

    suspend operator fun invoke(): YouTubeMusicVanced =
        repository.getYouTubeMusicVancedInformation()
}