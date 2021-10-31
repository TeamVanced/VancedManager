package com.vanced.manager.di

import com.vanced.manager.core.installer.impl.MicrogInstaller
import com.vanced.manager.core.installer.impl.MusicInstaller
import com.vanced.manager.core.installer.impl.VancedInstaller
import org.koin.dsl.module

val installerModule = module {

    fun provideVancedInstaller() = VancedInstaller()

    fun provideMusicInstaller() = MusicInstaller()

    fun provideMicrogInstaller() = MicrogInstaller()

    single { provideVancedInstaller() }
    single { provideMusicInstaller() }
    single { provideMicrogInstaller() }
}