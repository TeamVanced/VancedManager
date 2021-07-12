package com.vanced.manager.di

import com.vanced.manager.downloader.api.VancedAPI
import com.vanced.manager.downloader.impl.VancedDownloader
import com.vanced.manager.installer.VancedInstaller
import org.koin.dsl.module

val downloaderModule = module {

    fun provideVancedDownloader(
        vancedAPI: VancedAPI,
        vancedInstaller: VancedInstaller
    ): VancedDownloader = VancedDownloader(vancedAPI, vancedInstaller)

    fun provideMusicDownloader(
        vancedAPI: VancedAPI,
        vancedInstaller: VancedInstaller
    ): VancedDownloader = VancedDownloader(vancedAPI, vancedInstaller)

    fun provideMicrogDownloader(
        vancedAPI: VancedAPI,
        vancedInstaller: VancedInstaller
    ): VancedDownloader = VancedDownloader(vancedAPI, vancedInstaller)

    single { provideVancedDownloader(get(), get()) }
    single { provideMusicDownloader(get(), get()) }
    single { provideMicrogDownloader(get(), get()) }

}