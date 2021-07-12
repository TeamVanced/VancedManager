package com.vanced.manager.di

import com.vanced.manager.domain.datasource.PackageInformationDataSource
import com.vanced.manager.downloader.impl.VancedDownloader
import com.vanced.manager.network.model.AppDtoMapper
import com.vanced.manager.network.model.JsonDtoMapper
import org.koin.dsl.module

val mapperModule = module {

    fun provideAppMapper(
        packageInformationDataSource: PackageInformationDataSource,
    ): AppDtoMapper = AppDtoMapper(
        packageInformationDataSource = packageInformationDataSource
    )

    fun provideJsonMapper(
        appDtoMapper: AppDtoMapper
    ): JsonDtoMapper = JsonDtoMapper(
        appDtoMapper = appDtoMapper
    )

    single { provideAppMapper(get()) }
    single { provideJsonMapper(get()) }

}