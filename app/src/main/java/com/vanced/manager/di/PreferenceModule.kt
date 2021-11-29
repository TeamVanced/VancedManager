package com.vanced.manager.di

import android.content.Context
import org.koin.dsl.module

val preferenceModule = module {

    fun provideDatastore(
        context: Context
    ) = context.getSharedPreferences("manager_settings", Context.MODE_PRIVATE)

    single { provideDatastore(get()) }
}