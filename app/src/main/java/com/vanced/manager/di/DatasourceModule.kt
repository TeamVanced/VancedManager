package com.vanced.manager.di

import android.content.Context
import com.vanced.manager.repository.source.PkgInfoDatasource
import com.vanced.manager.repository.source.PkgInfoDatasourceImpl
import com.vanced.manager.repository.source.PreferenceDatasource
import com.vanced.manager.repository.source.PreferenceDatasourceImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val datasourceModule = module {

    fun providePkgInfoDatasource(
        context: Context
    ): PkgInfoDatasource {
        return PkgInfoDatasourceImpl(
            packageManager = context.packageManager
        )
    }

    fun providePreferenceDatasource(
        context: Context
    ): PreferenceDatasource {
        return PreferenceDatasourceImpl(
            sharedPreferences = context.getSharedPreferences(
                "manager_settings",
                Context.MODE_PRIVATE
            )
        )
    }

    single { providePkgInfoDatasource(androidContext()) }
    single { providePreferenceDatasource(androidContext()) }
}