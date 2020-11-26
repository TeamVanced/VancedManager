package com.vanced.manager.feature.home.domain.usecase

import com.vanced.manager.feature.home.domain.entity.MicroG
import com.vanced.manager.feature.home.domain.repository.AppInformationRepository

class GetMicroGInformationUseCase(
    private val repository: AppInformationRepository
) {

    suspend operator fun invoke(): MicroG =
        repository.getMicroGInformation()
}