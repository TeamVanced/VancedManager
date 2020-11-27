package com.vanced.manager.feature.home.domain.usecase

import com.vanced.manager.feature.home.domain.entity.YouTubeMusicVanced
import com.vanced.manager.feature.home.domain.repository.AppInformationRepository
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk

internal class GetYouTubeMusicVancedInformationUseCaseTest : ShouldSpec() {

    private val repository: AppInformationRepository = mockk()

    private val useCase = GetYouTubeMusicVancedInformationUseCase(repository)

    init {
        should("return information YouTubeMusicVanced") {
            val expectation = YouTubeMusicVanced("", 1, "", "")

            coEvery { repository.getYouTubeMusicVancedInformation() } returns expectation

            useCase() shouldBe expectation
        }
    }
}