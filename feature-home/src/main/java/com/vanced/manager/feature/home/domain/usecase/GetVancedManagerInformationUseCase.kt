package com.vanced.manager.feature.home.domain.usecase

import com.vanced.manager.feature.home.domain.entity.VancedManager
import com.vanced.manager.feature.home.domain.repository.AppInformationRepository

class GetVancedManagerInformationUseCase(
    private val repository: AppInformationRepository
) {

    suspend operator fun invoke(): VancedManager =
        repository.getVancedManagerInformation()
}