package com.vanced.manager.di

import android.app.Application
import com.vanced.manager.downloader.impl.MicrogDownloader
import com.vanced.manager.downloader.impl.MusicDownloader
import com.vanced.manager.downloader.impl.VancedDownloader
import com.vanced.manager.installer.impl.MicrogInstaller
import com.vanced.manager.installer.impl.MusicInstaller
import com.vanced.manager.installer.impl.VancedInstaller
import com.vanced.manager.repository.AppRepository
import com.vanced.manager.repository.PreferenceRepository
import com.vanced.manager.ui.viewmodel.ConfigurationViewModel
import com.vanced.manager.ui.viewmodel.InstallViewModel
import com.vanced.manager.ui.viewmodel.MainViewModel
import com.vanced.manager.ui.viewmodel.SettingsViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    fun provideMainViewModel(
        appRepository: AppRepository,
        preferenceRepository: PreferenceRepository,
        app: Application,
    ): MainViewModel {
        return MainViewModel(
            appRepository = appRepository,
            preferenceRepository = preferenceRepository,
            app = app
        )
    }

    fun provideInstallViewModel(
        vancedDownloader: VancedDownloader,
        musicDownloader: MusicDownloader,
        microgDownloader: MicrogDownloader,

        vancedInstaller: VancedInstaller,
        musicInstaller: MusicInstaller,
        microgInstaller: MicrogInstaller,
    ): InstallViewModel {
        return InstallViewModel(
            vancedDownloader = vancedDownloader,
            musicDownloader = musicDownloader,
            microgDownloader = microgDownloader,

            vancedInstaller = vancedInstaller,
            musicInstaller = musicInstaller,
            microgInstaller = microgInstaller
        )
    }

    fun provideConfigurationViewModel(): ConfigurationViewModel {
        return ConfigurationViewModel()
    }

    fun provideSettingsViewModel(
        preferenceRepository: PreferenceRepository
    ): SettingsViewModel {
        return SettingsViewModel(
            preferenceRepository = preferenceRepository
        )
    }

    viewModel { provideMainViewModel(get(), get(), androidApplication()) }
    viewModel { provideInstallViewModel(get(), get(), get(), get(), get(), get()) }
    viewModel { provideConfigurationViewModel() }
    viewModel { provideSettingsViewModel(get()) }
}