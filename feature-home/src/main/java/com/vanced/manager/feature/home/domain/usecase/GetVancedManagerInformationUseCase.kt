package com.vanced.manager.feature.home.domain.usecase

import com.vanced.manager.feature.home.domain.entity.App
import com.vanced.manager.feature.home.domain.repository.AppRepository

class GetVancedManagerInformationUseCase(
    private val repository: AppRepository
) {

    suspend operator fun invoke(
        packageName: (App.VancedManager.Companion) -> String
    ): App.VancedManager = repository.getVancedManagerInformation(packageName)
}