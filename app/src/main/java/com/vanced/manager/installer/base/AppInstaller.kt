package com.vanced.manager.installer.base

import com.vanced.manager.repository.manager.PackageManagerResult

abstract class AppInstaller {

    abstract suspend fun install(appVersions: List<String>?)

    abstract suspend fun installRoot(appVersions: List<String>?): PackageManagerResult<Nothing>

}