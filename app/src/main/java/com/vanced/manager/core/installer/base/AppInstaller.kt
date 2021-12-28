package com.vanced.manager.core.installer.base

import com.vanced.manager.core.installer.util.PMRootResult

abstract class AppInstaller {

    abstract fun install(appVersions: List<String>?)

    abstract fun installRoot(appVersions: List<String>?): PMRootResult<Nothing>

}