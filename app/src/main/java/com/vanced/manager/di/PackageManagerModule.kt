package com.vanced.manager.di

import android.content.Context
import com.vanced.manager.domain.datasource.PackageInformationDataSource
import com.vanced.manager.domain.datasource.PackageInformationDataSourceImpl
import com.vanced.manager.domain.pkg.PkgManager
import com.vanced.manager.domain.pkg.PkgManagerImpl
import org.koin.dsl.module

val packageManagerModule = module {

    fun providePackageManager(
        context: Context
    ): PkgManager {
        return PkgManagerImpl(
            packageManager = context.packageManager
        )
    }

    fun providePackageInformationDataSource(
        pkgManager: PkgManager
    ): PackageInformationDataSource {
        return PackageInformationDataSourceImpl(
            pkgManager = pkgManager
        )
    }

    single { providePackageManager(get()) }
    single { providePackageInformationDataSource(get()) }
}