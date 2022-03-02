package com.vanced.manager.installer.base

import com.vanced.manager.installer.util.PMRootResult

abstract class AppInstaller {

    abstract fun install(appVersions: List<String>?)

    abstract fun installRoot(appVersions: List<String>?): PMRootResult<Nothing>

}