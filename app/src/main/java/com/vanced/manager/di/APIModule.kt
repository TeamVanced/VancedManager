package com.vanced.manager.di

import com.vanced.manager.downloader.api.VancedAPI
import com.vanced.manager.network.util.BASE
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit

val apiModule = module {

    fun provideVancedAPI(
        okHttpClient: OkHttpClient
    ): VancedAPI = Retrofit.Builder()
        .baseUrl(BASE)
        .client(okHttpClient)
        .build()
        .create(VancedAPI::class.java)

    single { provideVancedAPI(get()) }

}