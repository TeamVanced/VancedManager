package com.vanced.manager.di

import android.content.Context
import com.vanced.manager.domain.datasource.PackageInformationDataSource
import com.vanced.manager.network.model.AppDtoMapper
import com.vanced.manager.network.model.DataDtoMapper
import org.koin.dsl.module

val mapperModule = module {

    fun provideAppMapper(
        packageInformationDataSource: PackageInformationDataSource,
        context: Context,
    ) = AppDtoMapper(packageInformationDataSource, context)

    fun provideJsonMapper(
        appDtoMapper: AppDtoMapper
    ) = DataDtoMapper(appDtoMapper)

    single { provideAppMapper(get(), get()) }
    single { provideJsonMapper(get()) }
}