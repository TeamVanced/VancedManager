package com.vanced.manager.feature.home.data.datasource

import android.content.pm.PackageManager
import com.vanced.manager.feature.home.data.pkg.PkgManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface PkgInformationDataSource {

    @Throws(PackageManager.NameNotFoundException::class)
    suspend fun getVersionCode(packageName: String): Int

    @Throws(PackageManager.NameNotFoundException::class)
    suspend fun getVersionName(packageName: String): String
}

class PkgInformationDataSourceImpl(
    private val pkgManager: PkgManager
) : PkgInformationDataSource {

    override suspend fun getVersionCode(packageName: String): Int =
        withContext(Dispatchers.IO) {
            pkgManager.getVersionCode(packageName)
        }

    override suspend fun getVersionName(packageName: String): String =
        withContext(Dispatchers.IO) {
            pkgManager.getVersionName(packageName)
        }
}