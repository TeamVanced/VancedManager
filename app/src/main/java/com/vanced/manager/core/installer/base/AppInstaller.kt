package com.vanced.manager.core.installer.base

abstract class AppInstaller {

    abstract fun install(appVersions: List<String>?)

    abstract fun installRoot(appVersions: List<String>?)

}