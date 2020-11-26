package com.vanced.manager.feature.home.data.datasource

import com.vanced.manager.feature.home.data.api.GetAppInformationApi
import com.vanced.manager.feature.home.data.dto.toEntity
import com.vanced.manager.feature.home.domain.entity.VancedApps
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface AppInformationDataSource {

    suspend fun getAppInformation(): VancedApps
}

class AppInformationDataSourceImpl(
    private val api: GetAppInformationApi
) : AppInformationDataSource {

    override suspend fun getAppInformation(): VancedApps =
        withContext(Dispatchers.IO) {
            api.getAppInformation().toEntity()
        }
}