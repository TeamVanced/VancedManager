package com.vanced.manager.di

import com.vanced.manager.downloader.api.MicrogAPI
import com.vanced.manager.downloader.api.MusicAPI
import com.vanced.manager.downloader.api.VancedAPI
import com.vanced.manager.downloader.impl.MicrogDownloader
import com.vanced.manager.downloader.impl.MusicDownloader
import com.vanced.manager.downloader.impl.VancedDownloader
import com.vanced.manager.installer.impl.MicrogInstaller
import com.vanced.manager.installer.impl.MusicInstaller
import com.vanced.manager.installer.impl.VancedInstaller
import org.koin.dsl.module

val downloaderModule = module {

    fun provideVancedDownloader(
        vancedInstaller: VancedInstaller,
        vancedAPI: VancedAPI,
    ) = VancedDownloader(vancedInstaller, vancedAPI)

    fun provideMusicDownloader(
        musicInstaller: MusicInstaller,
        musicAPI: MusicAPI,
    ) = MusicDownloader(musicInstaller, musicAPI)

    fun provideMicrogDownloader(
        microgInstaller: MicrogInstaller,
        microgAPI: MicrogAPI,
    ) = MicrogDownloader(microgInstaller, microgAPI)

    single { provideVancedDownloader(get(), get()) }
    single { provideMusicDownloader(get(), get()) }
    single { provideMicrogDownloader(get(), get()) }
}