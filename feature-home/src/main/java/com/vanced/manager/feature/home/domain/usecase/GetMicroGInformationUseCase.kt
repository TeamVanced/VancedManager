package com.vanced.manager.feature.home.domain.usecase

import com.vanced.manager.feature.home.domain.entity.App
import com.vanced.manager.feature.home.domain.repository.AppRepository

class GetMicroGInformationUseCase(
    private val repository: AppRepository
) {

    suspend operator fun invoke(
        packageName: (App.MicroG.Companion) -> String
    ): App.MicroG = repository.getMicroGInformation(packageName)
}