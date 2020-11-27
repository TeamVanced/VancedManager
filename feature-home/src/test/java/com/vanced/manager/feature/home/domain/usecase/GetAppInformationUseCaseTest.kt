package com.vanced.manager.feature.home.domain.usecase

import com.vanced.manager.feature.home.domain.entity.*
import com.vanced.manager.feature.home.domain.repository.AppInformationRepository
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk

internal class GetAppInformationUseCaseTest : ShouldSpec() {

    private val repository: AppInformationRepository = mockk()

    private val useCase = GetAppInformationUseCase(repository)

    init { // https://habr.com/ru/post/520380/
        should("return information all aps") {
            val expectation = VancedApps(
                VancedManager("", 1, "", ""),
                YouTubeVanced("", 1, "", "", listOf(), listOf()),
                YouTubeMusicVanced("", 1, "", ""),
                MicroG("", 1, "", "")
            )

            coEvery { repository.getAppInformation() } returns expectation

            useCase() shouldBe expectation
        }
    }
}