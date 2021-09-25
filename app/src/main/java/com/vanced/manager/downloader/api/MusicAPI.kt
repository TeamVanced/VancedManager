package com.vanced.manager.downloader.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Streaming

interface MusicAPI {

    @GET("music/v{version}/{variant}.apk")
    @Streaming
    fun getFiles(
        @Path("version") version: String,
        @Path("variant") variant: String,
    ): Call<ResponseBody>

}