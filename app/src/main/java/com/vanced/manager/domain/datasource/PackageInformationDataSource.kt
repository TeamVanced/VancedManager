package com.vanced.manager.domain.datasource

import android.content.pm.PackageManager
import com.vanced.manager.domain.pkg.PkgManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface PackageInformationDataSource {

    suspend fun getVersionCode(packageName: String): Int?

    suspend fun getVersionName(packageName: String): String?
}

class PackageInformationDataSourceImpl(
    private val pkgManager: PkgManager
) : PackageInformationDataSource {

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