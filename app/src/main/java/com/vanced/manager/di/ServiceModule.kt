package com.vanced.manager.di

import com.google.gson.GsonBuilder
import com.vanced.manager.network.DataService
import com.vanced.manager.network.util.BASE_GITHUB
import com.vanced.manager.network.util.BASE_MIRROR
import okhttp3.OkHttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val serviceModule = module {

    fun provideMainService(
        okHttpClient: OkHttpClient
    ) = Retrofit.Builder()
        .baseUrl(BASE_GITHUB)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .client(okHttpClient)
        .build()
        .create(DataService::class.java)

    fun provideMirrorService(
        okHttpClient: OkHttpClient
    ) = Retrofit.Builder()
        .baseUrl(BASE_MIRROR)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .client(okHttpClient)
        .build()
        .create(DataService::class.java)

    single(named("main")) { provideMainService(get()) }
    single(named("mirror")) { provideMirrorService(get()) }
}