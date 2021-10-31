package com.vanced.manager.domain.model

data class App(
    val name: String,
    val remoteVersion: String,
    val remoteVersionCode: Int,
    val installedVersion: String?,
    val installedVersionCode: Int?,
    val installedVersionRoot: String?,
    val installedVersionCodeRoot: Int?,
    val iconUrl: String?,
    val appStatus: AppStatus,
    val appStatusRoot: AppStatus,
    val packageName: String,
    val packageNameRoot: String?,
    val changelog: String,
    val url: String?,
    val versions: List<String>?,
    val themes: List<String>?,
    val languages: List<String>?,
    val installationOptions: List<InstallationOption>?
)