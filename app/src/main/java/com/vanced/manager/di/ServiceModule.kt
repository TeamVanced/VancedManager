package com.vanced.manager.di

import com.google.gson.GsonBuilder
import com.vanced.manager.network.JsonService
import com.vanced.manager.network.util.baseGithubUrl
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val serviceModule = module {

    fun provideRetrofitService(okHttpClient: OkHttpClient): JsonService =
        Retrofit.Builder()
            .baseUrl(baseGithubUrl)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .client(okHttpClient)
            .build()
            .create(JsonService::class.java)

    single { provideRetrofitService(get()) }

}