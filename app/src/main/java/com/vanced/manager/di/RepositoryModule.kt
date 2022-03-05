package com.vanced.manager.di

import com.vanced.manager.network.GithubService
import com.vanced.manager.repository.AppRepository
import com.vanced.manager.repository.AppRepositoryImpl
import com.vanced.manager.repository.PreferenceRepository
import com.vanced.manager.repository.PreferenceRepositoryImpl
import com.vanced.manager.repository.manager.NonrootPackageManager
import com.vanced.manager.repository.manager.RootPackageManager
import com.vanced.manager.repository.source.PreferenceDatasource
import org.koin.dsl.module

val repositoryModule = module {

    fun provideGithubRepository(
        githubService: GithubService,
        nonrootPackageManager: NonrootPackageManager,
        rootPackageManager: RootPackageManager,
    ): AppRepository {
        return AppRepositoryImpl(
            githubService = githubService,
            nonrootPackageManager = nonrootPackageManager,
            rootPackageManager = rootPackageManager
        )
    }

    fun providePreferenceRepository(
        preferenceDatasource: PreferenceDatasource
    ): PreferenceRepository {
        return PreferenceRepositoryImpl(
            preferenceDatasource = preferenceDatasource
        )
    }

    single { provideGithubRepository(get(), get(), get()) }
    single { providePreferenceRepository(get()) }
}