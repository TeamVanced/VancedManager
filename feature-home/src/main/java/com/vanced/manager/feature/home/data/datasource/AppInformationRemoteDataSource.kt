package com.vanced.manager.feature.home.data.datasource

import com.vanced.manager.feature.home.data.api.GetAppInformationApi
import com.vanced.manager.feature.home.data.dto.toEntity
import com.vanced.manager.feature.home.domain.entity.MicroGInfo
import com.vanced.manager.feature.home.domain.entity.VancedManagerInfo
import com.vanced.manager.feature.home.domain.entity.YouTubeMusicVancedInfo
import com.vanced.manager.feature.home.domain.entity.YouTubeVancedInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface AppInformationRemoteDataSource {

    suspend fun getMicroGInformation(): MicroGInfo

    suspend fun getVancedManagerInformation(): VancedManagerInfo

    suspend fun getYouTubeMusicVancedInformation(): YouTubeMusicVancedInfo

    suspend fun getYouTubeVancedInformation(): YouTubeVancedInfo
}

class AppInformationDataSourceImpl(
    private val api: GetAppInformationApi
) : AppInformationRemoteDataSource {

    override suspend fun getMicroGInformation(): MicroGInfo =
        withContext(Dispatchers.IO) {
            api.getAppInformation().microG.toEntity()
        }

    override suspend fun getVancedManagerInformation(): VancedManagerInfo =
        withContext(Dispatchers.IO) {
            api.getAppInformation().vancedManager.toEntity()
        }

    override suspend fun getYouTubeMusicVancedInformation(): YouTubeMusicVancedInfo =
        withContext(Dispatchers.IO) {
            api.getAppInformation().youTubeMusicVanced.toEntity()
        }

    override suspend fun getYouTubeVancedInformation(): YouTubeVancedInfo =
        withContext(Dispatchers.IO) {
            api.getAppInformation().youTubeVanced.toEntity()
        }
}