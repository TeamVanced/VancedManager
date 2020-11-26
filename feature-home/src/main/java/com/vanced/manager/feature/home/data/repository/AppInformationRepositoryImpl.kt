package com.vanced.manager.feature.home.data.repository

import com.vanced.manager.feature.home.data.datasource.AppInformationDataSource
import com.vanced.manager.feature.home.domain.entity.*
import com.vanced.manager.feature.home.domain.repository.AppInformationRepository

class AppInformationRepositoryImpl(
    private val dataSource: AppInformationDataSource
) : AppInformationRepository {

    override suspend fun getAppInformation(): VancedApps =
        dataSource.getAppInformation()

    override suspend fun getMicroGInformation(): MicroG =
        dataSource.getAppInformation().microG

    override suspend fun getVancedManagerInformation(): VancedManager =
        dataSource.getAppInformation().vancedManager

    override suspend fun getYouTubeMusicVancedInformation(): YouTubeMusicVanced =
        dataSource.getAppInformation().youTubeMusicVanced

    override suspend fun getYouTubeVancedInformation(): YouTubeVanced =
        dataSource.getAppInformation().youTubeVanced
}