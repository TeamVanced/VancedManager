package com.vanced.manager.library.network.service

import retrofit2.Retrofit

inline fun <reified T> createRetrofitService(retrofit: Retrofit): T =
    retrofit.create(T::class.java)