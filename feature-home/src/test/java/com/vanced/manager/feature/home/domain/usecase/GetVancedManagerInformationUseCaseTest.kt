package com.vanced.manager.feature.home.domain.usecase

import com.vanced.manager.feature.home.domain.entity.VancedManager
import com.vanced.manager.feature.home.domain.repository.AppInformationRepository
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk

internal class GetVancedManagerInformationUseCaseTest : ShouldSpec() {

    private val repository: AppInformationRepository = mockk()

    private val useCase = GetVancedManagerInformationUseCase(repository)

    init { // https://dev.to/kotest/testing-kotlin-js-with-kotest-i2j
        should("return information VancedManager") {
            val expectation = VancedManager("", 1, "", "")

            coEvery { repository.getVancedManagerInformation() } returns expectation

            useCase() shouldBe expectation
        }
    }
}