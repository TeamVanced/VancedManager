package com.vanced.manager.di

import android.content.Context
import com.vanced.manager.installer.impl.MicrogInstaller
import com.vanced.manager.installer.impl.MusicInstaller
import com.vanced.manager.installer.impl.VancedInstaller
import com.vanced.manager.repository.manager.NonrootPackageManager
import com.vanced.manager.repository.manager.RootPackageManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val installerModule = module {

    fun provideVancedInstaller(
        context: Context,
        nonrootPackageManager: NonrootPackageManager,
        rootPackageManager: RootPackageManager
    ): VancedInstaller {
        return VancedInstaller(
            context = context,
            nonrootPackageManager = nonrootPackageManager,
            rootPackageManager = rootPackageManager
        )
    }

    fun provideMusicInstaller(
        context: Context,
        nonrootPackageManager: NonrootPackageManager,
        rootPackageManager: RootPackageManager
    ): MusicInstaller {
        return MusicInstaller(
            context = context,
            nonrootPackageManager = nonrootPackageManager,
            rootPackageManager = rootPackageManager
        )
    }

    fun provideMicrogInstaller(
        context: Context,
        nonrootPackageManager: NonrootPackageManager,
    ): MicrogInstaller {
        return MicrogInstaller(
            context = context,
            nonrootPackageManager = nonrootPackageManager
        )
    }

    single { provideVancedInstaller(androidContext(), get(), get()) }
    single { provideMusicInstaller(androidContext(), get(), get()) }
    single { provideMicrogInstaller(androidContext(), get()) }
}