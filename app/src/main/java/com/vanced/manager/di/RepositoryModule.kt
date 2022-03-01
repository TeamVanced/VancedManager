package com.vanced.manager.di

import com.vanced.manager.datasource.PkgInfoDatasource
import com.vanced.manager.datasource.PreferenceDatasource
import com.vanced.manager.network.GithubService
import com.vanced.manager.repository.*
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