package com.vanced.manager.feature.home.domain.usecase

import com.vanced.manager.feature.home.domain.entity.YouTubeVanced
import com.vanced.manager.feature.home.domain.repository.AppInformationRepository

class GetYouTubeVancedInformationUseCase(
    private val repository: AppInformationRepository
) {

    suspend operator fun invoke(): YouTubeVanced =
        repository.getYouTubeVancedInformation()
}