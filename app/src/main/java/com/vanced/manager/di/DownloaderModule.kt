package com.vanced.manager.di

import com.vanced.manager.downloader.api.VancedAPI
import com.vanced.manager.downloader.impl.MicrogDownloader
import com.vanced.manager.downloader.impl.MusicDownloader
import com.vanced.manager.downloader.impl.VancedDownloader
import com.vanced.manager.installer.MicrogInstaller
import com.vanced.manager.installer.MusicInstaller
import com.vanced.manager.installer.VancedInstaller
import org.koin.dsl.module

val downloaderModule = module {

    fun provideVancedDownloader(
        vancedAPI: VancedAPI,
        vancedInstaller: VancedInstaller
    ) = VancedDownloader(vancedAPI, vancedInstaller)

    fun provideMusicDownloader(
        musicInstaller: MusicInstaller
    ) = MusicDownloader(musicInstaller)

    fun provideMicrogDownloader(
        microgInstaller: MicrogInstaller
    ) = MicrogDownloader(microgInstaller)

    single { provideVancedDownloader(get(), get()) }
    single { provideMusicDownloader(get()) }
    single { provideMicrogDownloader(get()) }

}