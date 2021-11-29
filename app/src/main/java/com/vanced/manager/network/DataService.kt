package com.vanced.manager.network

import com.vanced.manager.network.model.DataDto
import retrofit2.http.GET

interface DataService {

    @GET("latest.json")
    suspend fun get(): DataDto

}