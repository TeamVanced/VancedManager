package com.vanced.manager.di

import okhttp3.OkHttpClient
import org.koin.dsl.module

val networkModule = module {

    fun provideOkHttpClient() =
        OkHttpClient()

    single { provideOkHttpClient() }
}