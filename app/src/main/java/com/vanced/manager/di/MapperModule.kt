package com.vanced.manager.di

import com.vanced.manager.network.model.AppDtoMapper
import com.vanced.manager.network.model.JsonDtoMapper
import org.koin.dsl.module

val mapperModule = module {

    fun provideAppMapper(): AppDtoMapper = AppDtoMapper()

    fun provideJsonMapper(
        appDtoMapper: AppDtoMapper
    ): JsonDtoMapper = JsonDtoMapper(
        appDtoMapper = appDtoMapper
    )

    single { provideAppMapper() }
    single { provideJsonMapper(get()) }
}