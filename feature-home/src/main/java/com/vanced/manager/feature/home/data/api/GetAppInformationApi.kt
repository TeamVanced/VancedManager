package com.vanced.manager.feature.home.data.api

import com.vanced.manager.feature.home.data.dto.VancedAppsDto
import retrofit2.http.GET

interface GetAppInformationApi {

    companion object {
        const val URL = "/api/v1/latest.json"
    }

    @GET(URL)
    suspend fun getAppInformation(): VancedAppsDto
}