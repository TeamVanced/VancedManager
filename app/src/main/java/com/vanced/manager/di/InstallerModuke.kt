package com.vanced.manager.di

import android.content.Context
import com.vanced.manager.installer.impl.MicrogInstaller
import com.vanced.manager.installer.impl.MusicInstaller
import com.vanced.manager.installer.impl.VancedInstaller
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val installerModule = module {

    fun provideVancedInstaller(
        context: Context
    ): VancedInstaller {
        return VancedInstaller(
            context = context
        )
    }

    fun provideMusicInstaller(
        context: Context
    ): MusicInstaller {
        return MusicInstaller(
            context = context
        )
    }

    fun provideMicrogInstaller(
        context: Context
    ): MicrogInstaller {
        return MicrogInstaller(
            context = context
        )
    }

    single { provideVancedInstaller(androidContext()) }
    single { provideMusicInstaller(androidContext()) }
    single { provideMicrogInstaller(androidContext()) }
}