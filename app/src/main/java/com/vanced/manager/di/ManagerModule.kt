package com.vanced.manager.di

import android.content.Context
import com.vanced.manager.repository.manager.NonrootPackageManager
import com.vanced.manager.repository.manager.RootPackageManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val managerModule = module {

    fun provideNonrootPackageManager(
        context: Context
    ): NonrootPackageManager {
        return NonrootPackageManager(
            context = context
        )
    }

    fun provideRootPackageManager(): RootPackageManager {
        return RootPackageManager()
    }

    single { provideNonrootPackageManager(androidContext()) }
    single { provideRootPackageManager() }
}