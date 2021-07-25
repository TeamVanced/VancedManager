package com.vanced.manager.di

import com.vanced.manager.network.JsonService
import com.vanced.manager.network.model.JsonDtoMapper
import com.vanced.manager.repository.JsonRepository
import com.vanced.manager.repository.JsonRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {

    fun provideJsonRepository(
        jsonService: JsonService,
        jsonDtoMapper: JsonDtoMapper
    ): JsonRepository = JsonRepositoryImpl(
        jsonService,
        jsonDtoMapper
    )

    single { provideJsonRepository(get(), get()) }
}