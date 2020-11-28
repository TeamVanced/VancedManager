package com.vanced.manager.feature.home.data.api

import com.vanced.manager.feature.home.data.dto.AppInfoDto
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Url

interface AppsApi {

    @GET
    suspend fun getAppsInfo(@Url url: String): List<AppInfoDto>

    @GET
    suspend fun getIcon(@Url url: String): ResponseBody
}