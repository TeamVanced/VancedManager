package com.vanced.manager.feature.home.domain.repository

import com.vanced.manager.feature.home.domain.entity.AppInfo

interface AppsRepository {

    suspend fun getAppsInfo(): List<AppInfo>
}