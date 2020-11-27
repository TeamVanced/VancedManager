package com.vanced.manager.feature.home.data.repository

import com.vanced.manager.feature.home.data.datasource.AppInformationDataSource
import com.vanced.manager.feature.home.domain.entity.*
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk

internal class AppInformationRepositoryImplTest : ShouldSpec() {

    private val dataSource: AppInformationDataSource = mockk()

    private val repository = AppInformationRepositoryImpl(dataSource)

    private val expectation = VancedApps(
        VancedManager("", 1, "", ""),
        YouTubeVanced("", 1, "", "", listOf(), listOf()),
        YouTubeMusicVanced("", 1, "", ""),
        MicroG("", 1, "", "")
    )

    init { // https://kotest.io/styles/
        context("return information") {
            should("all apps") {
                coEvery { dataSource.getAppInformation() } returns expectation
                repository.getAppInformation() shouldBe expectation
            }

            should("VancedManager") {
                coEvery { dataSource.getAppInformation() } returns expectation
                repository.getVancedManagerInformation() shouldBe expectation.vancedManager
            }

            should("YouTubeVanced") {
                coEvery { dataSource.getAppInformation() } returns expectation
                repository.getYouTubeVancedInformation() shouldBe expectation.youTubeVanced
            }

            should("YouTubeMusicVanced") {
                coEvery { dataSource.getAppInformation() } returns expectation
                repository.getYouTubeMusicVancedInformation() shouldBe expectation.youTubeMusicVanced
            }

            should("MicroG") {
                coEvery { dataSource.getAppInformation() } returns expectation
                repository.getMicroGInformation() shouldBe expectation.microG
            }
        }
    }
}