package com.vanced.manager.di

import android.app.Application
import com.vanced.manager.core.downloader.impl.MicrogDownloader
import com.vanced.manager.core.downloader.impl.MusicDownloader
import com.vanced.manager.core.downloader.impl.VancedDownloader
import com.vanced.manager.core.installer.impl.MicrogInstaller
import com.vanced.manager.core.installer.impl.MusicInstaller
import com.vanced.manager.core.installer.impl.VancedInstaller
import com.vanced.manager.repository.DataRepository
import com.vanced.manager.repository.MainRepository
import com.vanced.manager.repository.MirrorRepository
import com.vanced.manager.ui.viewmodel.ConfigurationViewModel
import com.vanced.manager.ui.viewmodel.InstallViewModel
import com.vanced.manager.ui.viewmodel.MainViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    fun provideMainViewModel(
        mainRepository: MainRepository,
        mirrorRepository: MirrorRepository,
        app: Application,
    ) = MainViewModel(mainRepository, mirrorRepository, app)

    fun provideInstallViewModel(
        vancedDownloader: VancedDownloader,
        musicDownloader: MusicDownloader,
        microgDownloader: MicrogDownloader,

        vancedInstaller: VancedInstaller,
        musicInstaller: MusicInstaller,
        microgInstaller: MicrogInstaller,
    ) = InstallViewModel(vancedDownloader, musicDownloader, microgDownloader, vancedInstaller, musicInstaller, microgInstaller)

    fun provideConfigurationViewModel(): ConfigurationViewModel {
        return ConfigurationViewModel()
    }

    viewModel { provideMainViewModel(get(), get(), androidApplication()) }
    viewModel { provideInstallViewModel(get(), get(), get(), get(), get(), get()) }
    viewModel { provideConfigurationViewModel() }
}