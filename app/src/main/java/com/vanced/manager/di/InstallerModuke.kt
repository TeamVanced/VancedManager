package com.vanced.manager.di

import android.content.Context
import com.vanced.manager.installer.AppInstaller
import com.vanced.manager.ui.viewmodel.HomeViewModel
import org.koin.dsl.module

val installerModule = module {
    fun provideAppInstaller(
        context: Context,
        homeViewModel: HomeViewModel
    ) = AppInstaller(context, homeViewModel)

    single { provideAppInstaller(get(), get()) }
}