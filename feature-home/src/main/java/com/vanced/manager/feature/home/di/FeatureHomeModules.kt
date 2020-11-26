package com.vanced.manager.feature.home.di

import com.vanced.manager.feature.home.data.api.GetAppInformationApi
import com.vanced.manager.feature.home.data.datasource.AppInformationDataSource
import com.vanced.manager.feature.home.data.datasource.AppInformationDataSourceImpl
import com.vanced.manager.feature.home.data.repository.AppInformationRepositoryImpl
import com.vanced.manager.feature.home.domain.repository.AppInformationRepository
import com.vanced.manager.feature.home.domain.usecase.*
import com.vanced.manager.feature.home.presentation.HomeViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val retrofitModule = module {
    single<GetAppInformationApi?> { null }
}

internal val dataSourceModule = module {
    single<AppInformationDataSource> { AppInformationDataSourceImpl(api = get()) }
}

internal val repositoryModule = module {
    single<AppInformationRepository> { AppInformationRepositoryImpl(dataSource = get()) }
}

internal val useCaseModule = module {
    single { GetAppInformationUseCase(repository = get()) }

    single { GetMicroGInformationUseCase(repository = get()) }
    single { GetVancedManagerInformationUseCase(repository = get()) }
    single { GetYouTubeMusicVancedInformationUseCase(repository = get()) }
    single { GetYouTubeVancedInformationUseCase(repository = get()) }
}

internal val viewModelModule = module {
    viewModel {
        HomeViewModel(
            getAppInformationUseCase = get(),
            getMicroGInformationUseCase = get(),
            getVancedManagerInformationUseCase = get(),
            getYouTubeVancedInformationUseCase = get(),
            getYouTubeMusicVancedInformationUseCase = get()
        )
    }
}

val FeatureHomeModules = listOf(
    retrofitModule,
    dataSourceModule,
    repositoryModule,
    useCaseModule,
    viewModelModule
)