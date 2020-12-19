package com.vanced.manager.library.network.okhttp.interceptors

import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level

const val LOG_INTERCEPTOR = "LOG_INTERCEPTOR"

internal fun loggingInterceptor(): Interceptor =
    HttpLoggingInterceptor().setLevel(Level.BODY)