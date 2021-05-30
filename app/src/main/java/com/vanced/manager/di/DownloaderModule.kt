package com.vanced.manager.di

import com.vanced.manager.downloader.impl.VancedDownloader
import com.vanced.manager.downloader.api.VancedAPI
import com.vanced.manager.network.util.baseUrl
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit

val downloaderModule = module {

    fun provideVancedAPI(
        okHttpClient: OkHttpClient
    ): VancedAPI = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .build()
        .create(VancedAPI::class.java)

    fun provideVancedDownloader(
        vancedAPI: VancedAPI
    ): VancedDownloader = VancedDownloader(
        vancedAPI = vancedAPI
    )

    single { provideVancedAPI(get()) }
    single { provideVancedDownloader(get()) }

}