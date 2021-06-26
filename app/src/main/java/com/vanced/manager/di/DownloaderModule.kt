package com.vanced.manager.di

import com.vanced.manager.downloader.api.VancedAPI
import com.vanced.manager.downloader.impl.VancedDownloader
import org.koin.dsl.module

val downloaderModule = module {

    fun provideVancedDownloader(
        vancedAPI: VancedAPI,
    ): VancedDownloader = VancedDownloader(vancedAPI)

    single {
        provideVancedDownloader(get())
    }

}