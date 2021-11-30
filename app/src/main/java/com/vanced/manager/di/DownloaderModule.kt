package com.vanced.manager.di

import android.content.Context
import com.vanced.manager.core.downloader.api.MicrogAPI
import com.vanced.manager.core.downloader.api.MusicAPI
import com.vanced.manager.core.downloader.api.VancedAPI
import com.vanced.manager.core.downloader.impl.MicrogDownloader
import com.vanced.manager.core.downloader.impl.MusicDownloader
import com.vanced.manager.core.downloader.impl.VancedDownloader
import org.koin.dsl.module

val downloaderModule = module {

    fun provideVancedDownloader(
        vancedAPI: VancedAPI,
        context: Context,
    ) = VancedDownloader(vancedAPI, context)

    fun provideMusicDownloader(
        musicAPI: MusicAPI,
        context: Context,
    ) = MusicDownloader(musicAPI, context)

    fun provideMicrogDownloader(
        microgAPI: MicrogAPI,
        context: Context,
    ) = MicrogDownloader(microgAPI, context)

    single { provideVancedDownloader(get(), get()) }
    single { provideMusicDownloader(get(), get()) }
    single { provideMicrogDownloader(get(), get()) }
}