package com.vanced.manager.feature.home.data.datasource

import com.vanced.manager.feature.home.data.api.AppsApi
import com.vanced.manager.feature.home.data.dto.AppInfoDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface AppsInfoDataSource {

    suspend fun getAppsInfo(channel: String): List<AppInfoDto>
}

class AppsInfoDataSourceImpl(
    private val api: AppsApi
) : AppsInfoDataSource {

    override suspend fun getAppsInfo(channel: String): List<AppInfoDto> =
        withContext(Dispatchers.IO) {
            api.getAppsInfo(channel)
        }
}