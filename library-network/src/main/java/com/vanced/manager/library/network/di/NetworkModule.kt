package com.vanced.manager.library.network.di

import com.vanced.manager.library.network.okhttp.interceptors.LOG_INTERCEPTOR
import com.vanced.manager.library.network.okhttp.interceptors.NO_CONNECT_INTERCEPTOR
import com.vanced.manager.library.network.okhttp.interceptors.loggingInterceptor
import com.vanced.manager.library.network.okhttp.interceptors.noConnectionInterceptor
import com.vanced.manager.library.network.providers.provideMoshi
import com.vanced.manager.library.network.providers.provideOkHttpClient
import com.vanced.manager.library.network.providers.provideRetrofit
import org.koin.android.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module


const val ORIGINAL = "ORIGINAL"

val NetworkModule = module {
    factory(named(LOG_INTERCEPTOR)) { loggingInterceptor() }
    factory(named(NO_CONNECT_INTERCEPTOR)) { noConnectionInterceptor(androidContext()) }

    factory(named(ORIGINAL)) {
        if (BuildConfig.DEBUG) {
            provideOkHttpClient(
                interceptors = listOf(
                    get(named(LOG_INTERCEPTOR)),
                    get(named(NO_CONNECT_INTERCEPTOR))
                )
            )
        } else {
            provideOkHttpClient(
                interceptors = listOf(
                    get(named(NO_CONNECT_INTERCEPTOR))
                )
            )
        }
    }

    factory { provideMoshi() }

    factory(named(ORIGINAL)) {
        provideRetrofit(
            moshi = get(),
            okHttpClient = get(named(ORIGINAL)),
            url = "https://www.haliksar.fun"
        )
    }
}