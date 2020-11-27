package com.vanced.manager.feature.home.domain.usecase

import com.vanced.manager.feature.home.domain.entity.YouTubeVanced
import com.vanced.manager.feature.home.domain.repository.AppInformationRepository
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk

internal class GetYouTubeVancedInformationUseCaseTest : ShouldSpec() {

    private val repository: AppInformationRepository = mockk()

    private val useCase = GetYouTubeVancedInformationUseCase(repository)

    init {
        should("return information YouTubeVanced") {
            val expectation = YouTubeVanced("", 1, "", "", listOf(), listOf())

            coEvery { repository.getYouTubeVancedInformation() } returns expectation

            useCase() shouldBe expectation
        }
    }
}