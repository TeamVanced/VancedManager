package com.vanced.manager.downloader.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Streaming

interface MicrogAPI {

    @GET("releases/latest/download/microg.apk")
    @Streaming
    fun getFile(): Call<ResponseBody>

}