package com.vanced.manager.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.vanced.manager.network.GithubService
import com.vanced.manager.network.util.GITHUB_API_BASE
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.create

private val json = Json {
    ignoreUnknownKeys = true
}

val serviceModule = module {

    fun provideGithubService(
        okHttpClient: OkHttpClient
    ): GithubService {
        return Retrofit.Builder()
            .baseUrl(GITHUB_API_BASE)
            .addConverterFactory(json.asConverterFactory(MediaType.get("application/json")))
            .client(okHttpClient)
            .build()
            .create()
    }

    single { provideGithubService(get()) }
}