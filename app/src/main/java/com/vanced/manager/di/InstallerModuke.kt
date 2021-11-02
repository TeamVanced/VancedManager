package com.vanced.manager.di

import android.content.Context
import com.vanced.manager.core.installer.impl.MicrogInstaller
import com.vanced.manager.core.installer.impl.MusicInstaller
import com.vanced.manager.core.installer.impl.VancedInstaller
import org.koin.dsl.module

val installerModule = module {

    fun provideVancedInstaller(
        context: Context
    ) = VancedInstaller(context)

    fun provideMusicInstaller(
        context: Context
    ) = MusicInstaller(context)

    fun provideMicrogInstaller(
        context: Context
    ) = MicrogInstaller(context)

    single { provideVancedInstaller(get()) }
    single { provideMusicInstaller(get()) }
    single { provideMicrogInstaller(get()) }
}