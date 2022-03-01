package com.vanced.manager.di

import android.content.Context
import com.vanced.manager.core.downloader.api.MicrogAPI
import com.vanced.manager.core.downloader.api.MusicAPI
import com.vanced.manager.core.downloader.api.VancedAPI
import com.vanced.manager.core.downloader.impl.MicrogDownloader
import com.vanced.manager.core.downloader.impl.MusicDownloader
import com.vanced.manager.core.downloader.impl.VancedDownloader
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val downloaderModule = module {

    fun provideVancedDownloader(
        vancedAPI: VancedAPI,
        context: Context,
    ): VancedDownloader {
        return VancedDownloader(
            vancedAPI = vancedAPI,
            context = context
        )
    }

    fun provideMusicDownloader(
        musicAPI: MusicAPI,
        context: Context,
    ): MusicDownloader {
        return MusicDownloader(
            musicAPI = musicAPI,
            context = context
        )
    }

    fun provideMicrogDownloader(
        microgAPI: MicrogAPI,
        context: Context,
    ): MicrogDownloader {
        return MicrogDownloader(
            microgAPI = microgAPI,
            context = context
        )
    }

    single { provideVancedDownloader(get(), androidContext()) }
    single { provideMusicDownloader(get(), androidContext()) }
    single { provideMicrogDownloader(get(), androidContext()) }
}