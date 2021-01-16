package com.vanced.manager.library.network.providers

import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import kotlin.reflect.KClass

fun provideRetrofit(
    okHttpClient: OkHttpClient,
    moshi: Moshi,
    url: String
): Retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .client(okHttpClient)
    .baseUrl(url)
    .build()

fun <S: Any> createService(service: KClass<S>, baseurl: String): S {
    return provideRetrofit(
        provideOkHttpClient(),
        provideMoshi(),
        baseurl
    ).create(service.java)
}