package com.vanced.manager.downloader.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Streaming
import retrofit2.http.Url

interface MicrogAPI {

    @GET("releases/latest/download/microg.apk")
    @Streaming
    fun getFile() : Call<ResponseBody>

}