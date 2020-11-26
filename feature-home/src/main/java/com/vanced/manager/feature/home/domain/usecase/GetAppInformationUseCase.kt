package com.vanced.manager.feature.home.domain.usecase

import com.vanced.manager.feature.home.domain.entity.VancedApps
import com.vanced.manager.feature.home.domain.repository.AppInformationRepository

class GetAppInformationUseCase(
    private val repository: AppInformationRepository
) {

    suspend operator fun invoke(): VancedApps =
        repository.getAppInformation()
}