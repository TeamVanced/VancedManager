package com.vanced.manager.library.network.providers

import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

private const val WaitTime = 0L
private const val ConnTime = 0L

fun provideOkHttpClient(
    interceptors: List<Interceptor> = emptyList(),
    authenticators: List<Authenticator> = emptyList()
): OkHttpClient =
    OkHttpClient.Builder()
        .callTimeout(WaitTime, TimeUnit.SECONDS)
        .readTimeout(WaitTime, TimeUnit.MILLISECONDS)
        .connectTimeout(ConnTime, TimeUnit.MILLISECONDS)
        .writeTimeout(WaitTime, TimeUnit.MILLISECONDS)
        .apply {
            interceptors.forEach { addInterceptor(it) }
            authenticators.forEach { authenticator(it) }
        }
        .build()