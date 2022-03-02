package com.vanced.manager.di

import com.vanced.manager.downloader.api.MicrogAPI
import com.vanced.manager.downloader.api.MusicAPI
import com.vanced.manager.downloader.api.VancedAPI
import com.vanced.manager.network.util.BASE
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.create

//TODO Add mirror support
val apiModule = module {

    fun provideVancedAPI(
        okHttpClient: OkHttpClient
    ): VancedAPI {
        return Retrofit.Builder()
            .baseUrl(BASE)
            .client(okHttpClient)
            .build()
            .create()
    }

    fun provideMusicAPI(
        okHttpClient: OkHttpClient
    ): MusicAPI {
        return Retrofit.Builder()
            .baseUrl(BASE)
            .client(okHttpClient)
            .build()
            .create()
    }

    fun provideMicrogAPI(
        okHttpClient: OkHttpClient
    ): MicrogAPI {
        return Retrofit.Builder()
            .baseUrl("https://github.com/YTVanced/VancedMicroG/")
            .client(okHttpClient)
            .build()
            .create(MicrogAPI::class.java)
    }

    single { provideVancedAPI(get()) }
    single { provideMusicAPI(get()) }
    single { provideMicrogAPI(get()) }
}