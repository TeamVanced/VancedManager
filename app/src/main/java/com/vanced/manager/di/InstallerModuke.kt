package com.vanced.manager.di

import com.vanced.manager.installer.MicrogInstaller
import com.vanced.manager.installer.MusicInstaller
import com.vanced.manager.installer.VancedInstaller
import org.koin.dsl.module

val installerModule = module {

    fun provideVancedInstaller() = VancedInstaller()

    fun provideMusicInstaller() = MusicInstaller()

    fun provideMicrogInstaller() = MicrogInstaller()

    single { provideVancedInstaller() }
    single { provideMusicInstaller() }
    single { provideMicrogInstaller() }
}