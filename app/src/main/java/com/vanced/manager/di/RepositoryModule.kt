package com.vanced.manager.di

import com.vanced.manager.network.DataService
import com.vanced.manager.network.model.DataDtoMapper
import com.vanced.manager.repository.DataRepository
import com.vanced.manager.repository.MainRepository
import com.vanced.manager.repository.MirrorRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {

    fun provideMainRepository(
        dataService: DataService,
        dataDtoMapper: DataDtoMapper
    ) = MainRepository(dataService, dataDtoMapper)

    fun provideMirrorRepository(
        dataService: DataService,
        dataDtoMapper: DataDtoMapper
    ) = MirrorRepository(dataService, dataDtoMapper)

    single { provideMainRepository(get(named("main")), get()) }
    single { provideMirrorRepository(get(named("mirror")), get()) }
}