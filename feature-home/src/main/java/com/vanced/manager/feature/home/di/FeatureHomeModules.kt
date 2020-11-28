package com.vanced.manager.feature.home.di

import com.vanced.manager.feature.home.data.api.GetAppInformationApi
import com.vanced.manager.feature.home.data.datasource.AppInformationDataSourceImpl
import com.vanced.manager.feature.home.data.datasource.AppInformationRemoteDataSource
import com.vanced.manager.feature.home.data.datasource.PkgInformationDataSource
import com.vanced.manager.feature.home.data.datasource.PkgInformationDataSourceImpl
import com.vanced.manager.feature.home.data.pkg.PkgManager
import com.vanced.manager.feature.home.data.pkg.PkgManagerImpl
import com.vanced.manager.feature.home.data.repository.AppRepositoryImpl
import com.vanced.manager.feature.home.domain.repository.AppRepository
import com.vanced.manager.feature.home.domain.usecase.GetMicroGInformationUseCase
import com.vanced.manager.feature.home.domain.usecase.GetVancedManagerInformationUseCase
import com.vanced.manager.feature.home.domain.usecase.GetYouTubeMusicVancedInformationUseCase
import com.vanced.manager.feature.home.domain.usecase.GetYouTubeVancedInformationUseCase
import com.vanced.manager.feature.home.presentation.HomeViewModel
import com.vanced.manager.library.network.service.createRetrofitService
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val retrofitModule = module {
    single<GetAppInformationApi> { createRetrofitService(get()) }
}

internal val pkgManagerModule = module {
    single<PkgManager> { PkgManagerImpl(androidContext().packageManager) }
}

internal val dataSourceModule = module {
    single<AppInformationRemoteDataSource> { AppInformationDataSourceImpl(api = get()) }

    single<PkgInformationDataSource> { PkgInformationDataSourceImpl(pkgManager = get()) }
}

internal val repositoryModule = module {
    single<AppRepository> {
        AppRepositoryImpl(
            remoteDtaSource = get(),
            localDataSource = get()
        )
    }
}

internal val useCaseModule = module {
    single { GetMicroGInformationUseCase(repository = get()) }
    single { GetVancedManagerInformationUseCase(repository = get()) }
    single { GetYouTubeMusicVancedInformationUseCase(repository = get()) }
    single { GetYouTubeVancedInformationUseCase(repository = get()) }
}

internal val viewModelModule = module {
    viewModel {
        HomeViewModel(
            getMicroGInformationUseCase = get(),
            getVancedManagerInformationUseCase = get(),
            getYouTubeMusicVancedInformationUseCase = get(),
            getYouTubeVancedInformationUseCase = get()
        )
    }
}

val FeatureHomeModules = listOf(
    retrofitModule,
    pkgManagerModule,
    dataSourceModule,
    repositoryModule,
    useCaseModule,
    viewModelModule
)