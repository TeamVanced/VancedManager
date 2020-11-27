package com.vanced.manager.feature.home.domain.usecase

import com.vanced.manager.feature.home.domain.entity.MicroG
import com.vanced.manager.feature.home.domain.repository.AppInformationRepository
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk

internal class GetMicroGInformationUseCaseTest : ShouldSpec() {

    private val repository: AppInformationRepository = mockk()

    private val useCase = GetMicroGInformationUseCase(repository)

    init { // https://github.com/mapbox/mapbox-navigation-android/blob/c5b8f6185ac1f22262664f14167da2d7ef2522e2/libnavigation-core/src/test/java/com/mapbox/navigation/core/routerefresh/RouteRefreshControllerTest.kt
        should("return information MicroG") {
            val expectation = MicroG("", 1, "", "")

            coEvery { repository.getMicroGInformation() } returns expectation

            useCase() shouldBe expectation
        }
    }
}