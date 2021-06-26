package com.vanced.manager.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import org.koin.dsl.module

val Context.dataStore by preferencesDataStore("manager_settings")

val preferenceModule = module {
    fun provideDatastore(
        context: Context
    ) = context.dataStore

    single { provideDatastore(get()) }
}