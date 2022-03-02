package com.vanced.manager.di

import com.vanced.manager.network.GithubService
import com.vanced.manager.repository.AppRepository
import com.vanced.manager.repository.AppRepositoryImpl
import com.vanced.manager.repository.PreferenceRepository
import com.vanced.manager.repository.PreferenceRepositoryImpl
import com.vanced.manager.repository.source.PkgInfoDatasource
import com.vanced.manager.repository.source.PreferenceDatasource
import org.koin.dsl.module

val repositoryModule = module {

    fun provideGithubRepository(
        githubService: GithubService,
        pkgInfoDatasource: PkgInfoDatasource
    ): AppRepository {
        return AppRepositoryImpl(
            githubService = githubService,
            pkgInfoDatasource = pkgInfoDatasource
        )
    }

    fun providePreferenceRepository(
        preferenceDatasource: PreferenceDatasource
    ): PreferenceRepository {
        return PreferenceRepositoryImpl(
            preferenceDatasource = preferenceDatasource
        )
    }

    single { provideGithubRepository(get(), get()) }
    single { providePreferenceRepository(get()) }
}