package com.vanced.manager.feature.home.data.datasource

import com.vanced.manager.feature.home.data.api.GetAppInformationApi
import com.vanced.manager.feature.home.data.dto.*
import com.vanced.manager.feature.home.domain.entity.MicroGInfo
import com.vanced.manager.feature.home.domain.entity.VancedManagerInfo
import com.vanced.manager.feature.home.domain.entity.YouTubeMusicVancedInfo
import com.vanced.manager.feature.home.domain.entity.YouTubeVancedInfo
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk

internal class AppInformationDataSourceImplTest : ShouldSpec() {

    private val api: GetAppInformationApi = mockk()

    private val dataSource = AppInformationDataSourceImpl(api)

    private val verifiable = VancedAppsDto(
        VancedManagerDto("", 1, "", ""),
        YouTubeVancedDto("", 1, "", "", listOf(), listOf()),
        YouTubeMusicVancedDto("", 1, "", ""),
        MicroGDto("", 1, "", "")
    )

    init { // https://kotest.io/styles/
        context("return information") {
            should("VancedManagerInfo") {
                coEvery { api.getAppInformation() } returns verifiable
                dataSource
                    .getVancedManagerInformation() shouldBe
                        VancedManagerInfo(
                            version = "",
                            versionCode = 1,
                            baseUrl = "",
                            changeLog = ""
                        )
            }
            should("YouTubeVancedInfo") {
                coEvery { api.getAppInformation() } returns verifiable
                dataSource
                    .getYouTubeVancedInformation() shouldBe
                        YouTubeVancedInfo(
                            version = "",
                            versionCode = 1,
                            baseUrl = "",
                            changeLog = "",
                            themes = listOf(),
                            langs = listOf()
                        )
            }
            should("YouTubeMusicVancedInfo") {
                coEvery { api.getAppInformation() } returns verifiable
                dataSource
                    .getYouTubeMusicVancedInformation() shouldBe
                        YouTubeMusicVancedInfo(
                            version = "",
                            versionCode = 1,
                            baseUrl = "",
                            changeLog = ""
                        )
            }
            should("MicroGInfo") {
                coEvery { api.getAppInformation() } returns verifiable
                dataSource
                    .getMicroGInformation() shouldBe
                        MicroGInfo(
                            version = "",
                            versionCode = 1,
                            baseUrl = "",
                            changeLog = ""
                        )
            }
        }
    }
}