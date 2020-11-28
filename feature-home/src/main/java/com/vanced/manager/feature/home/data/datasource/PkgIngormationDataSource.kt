package com.vanced.manager.feature.home.data.datasource

import android.content.pm.PackageManager
import com.vanced.manager.feature.home.data.pkg.PkgManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface PkgInformationDataSource {

    suspend fun getVersionCode(packageName: String): Int?

    suspend fun getVersionName(packageName: String): String?
}

class PkgInformationDataSourceImpl(
    private val pkgManager: PkgManager
) : PkgInformationDataSource {

    override suspend fun getVersionCode(packageName: String): Int? =
        withContext(Dispatchers.IO) {
            try {
                pkgManager.getVersionCode(packageName)
            } catch (exception: PackageManager.NameNotFoundException) {
                null
            }
        }

    override suspend fun getVersionName(packageName: String): String? =
        withContext(Dispatchers.IO) {
            try {
                pkgManager.getVersionName(packageName)
            } catch (exception: PackageManager.NameNotFoundException) {
                null
            }
        }
}