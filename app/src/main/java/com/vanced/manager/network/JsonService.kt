package com.vanced.manager.network

import com.vanced.manager.network.model.JsonDto
import retrofit2.http.GET

interface JsonService {

    @GET("latest.json")
    suspend fun get(): JsonDto

}