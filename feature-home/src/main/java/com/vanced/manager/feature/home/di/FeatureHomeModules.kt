package com.vanced.manager.feature.home.di

import com.vanced.manager.feature.home.data.api.AppsApi
import com.vanced.manager.feature.home.data.datasource.AppsInfoDataSource
import com.vanced.manager.feature.home.data.datasource.AppsInfoDataSourceImpl
import com.vanced.manager.feature.home.data.datasource.PkgInformationDataSource
import com.vanced.manager.feature.home.data.datasource.PkgInformationDataSourceImpl
import com.vanced.manager.feature.home.data.pkg.PkgManager
import com.vanced.manager.feature.home.data.pkg.PkgManagerImpl
import com.vanced.manager.feature.home.data.repository.AppsRepositoryImpl
import com.vanced.manager.feature.home.domain.repository.AppsRepository
import com.vanced.manager.feature.home.domain.usecase.GetAppsInfoUseCase
import com.vanced.manager.feature.home.presentation.HomeViewModel
import com.vanced.manager.library.network.service.createRetrofitService
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal val retrofitModule = module {
    single<AppsApi> { createRetrofitService(get()) }
}

internal val pkgManagerModule = module {
    single<PkgManager> { PkgManagerImpl(androidContext().packageManager) }
}

internal val dataSourceModule = module {
    single<AppsInfoDataSource> { AppsInfoDataSourceImpl(api = get()) }

    single<PkgInformationDataSource> { PkgInformationDataSourceImpl(pkgManager = get()) }
}

internal val channelModule = module {
    factory(named("channel")) {
        listOf(
            "https://vancedapp.com/api/v1/latest.json",
            "https://vancedapp.com/api/v1/latest_root.json",
            "https://vancedapp.custom.com/api/v1/latest.json",
            "https://vancedapp.custom.com/api/v1/latest_root.json"
        ).random()
    }
}

internal val repositoryModule = module {
    factory<AppsRepository> {
        AppsRepositoryImpl(
            appsInfoDataSource = get(),
            appIconDataSource = get(),
            pkgInformationDataSource = get(),
            channel = get(named("channel")) //TODO experiment
        )
    }
}

internal val useCaseModule = module {
    single { GetAppsInfoUseCase(repository = get()) }
}

internal val viewModelModule = module {
    viewModel { HomeViewModel(getAppsInfoUseCase = get()) }
}

val FeatureHomeModules = listOf(
    channelModule, //TODO experiment
    retrofitModule,
    pkgManagerModule,
    dataSourceModule,
    repositoryModule,
    useCaseModule,
    viewModelModule
)