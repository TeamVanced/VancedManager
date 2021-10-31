package com.vanced.manager.di

import com.vanced.manager.core.downloader.api.MicrogAPI
import com.vanced.manager.core.downloader.api.MusicAPI
import com.vanced.manager.core.downloader.api.VancedAPI
import com.vanced.manager.network.util.BASE
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit

val apiModule = module {

    fun provideVancedAPI(
        okHttpClient: OkHttpClient
    ) = Retrofit.Builder()
        .baseUrl(BASE)
        .client(okHttpClient)
        .build()
        .create(VancedAPI::class.java)

    fun provideMusicAPI(
        okHttpClient: OkHttpClient
    ) = Retrofit.Builder()
        .baseUrl(BASE)
        .client(okHttpClient)
        .build()
        .create(MusicAPI::class.java)

    fun provideMicrogAPI(
        okHttpClient: OkHttpClient
    ) = Retrofit.Builder()
        .baseUrl("https://github.com/YTVanced/VancedMicroG/")
        .client(okHttpClient)
        .build()
        .create(MicrogAPI::class.java)

    single { provideVancedAPI(get()) }
    single { provideMusicAPI(get()) }
    single { provideMicrogAPI(get()) }
}