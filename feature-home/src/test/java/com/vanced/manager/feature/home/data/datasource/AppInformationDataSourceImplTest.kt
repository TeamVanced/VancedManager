package com.vanced.manager.feature.home.data.datasource

import com.vanced.manager.feature.home.data.api.GetAppInformationApi
import com.vanced.manager.feature.home.data.dto.*
import com.vanced.manager.feature.home.domain.entity.*
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk

internal class AppInformationDataSourceImplTest : ShouldSpec() {

    private val api: GetAppInformationApi = mockk()

    private val dataSource = AppInformationDataSourceImpl(api)

    private val expectation = VancedApps(
        VancedManager("", 1, "", ""),
        YouTubeVanced("", 1, "", "", listOf(), listOf()),
        YouTubeMusicVanced("", 1, "", ""),
        MicroG("", 1, "", "")
    )

    private val verifiable = VancedAppsDto(
        VancedManagerDto("", 1, "", ""),
        YouTubeVancedDto("", 1, "", "", listOf(), listOf()),
        YouTubeMusicVancedDto("", 1, "", ""),
        MicroGDto("", 1, "", "")
    )

    init { // https://kotest.io/styles/
        context("return information") {
            should("all apps") {
                coEvery { api.getAppInformation() } returns verifiable
                dataSource.getAppInformation() shouldBe expectation
            }
        }
    }
}