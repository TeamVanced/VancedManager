package com.vanced.manager.di

import android.content.Context
import com.vanced.manager.repository.source.PreferenceDatasource
import com.vanced.manager.repository.source.PreferenceDatasourceImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val datasourceModule = module {

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

    single { providePreferenceDatasource(androidContext()) }
}