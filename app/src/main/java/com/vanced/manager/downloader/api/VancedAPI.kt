package com.vanced.manager.downloader.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Streaming

interface VancedAPI {

    @GET("apks/{version}/{variant}/{type}/{apkName}.apk")
    @Streaming
    fun getApk(
        @Path("version") version: String,
        @Path("variant") variant: String,
        @Path("type") type: String,
        @Path("apkName") apkName: String,
    ) : Call<ResponseBody>

}