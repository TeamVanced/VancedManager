package com.vanced.manager.domain.model

import com.vanced.manager.downloader.base.AppDownloader
import com.vanced.manager.ui.widgets.home.installation.InstallationOption

data class App(
    val name: String? = null,
    val remoteVersion: String? = null,
    val remoteVersionCode: Int? = null,
    val installedVersion: String? = null,
    val installedVersionCode: Int? = null,
    val installedVersionRoot: String? = null,
    val installedVersionCodeRoot: Int? = null,
    val iconUrl: String? = "",
    val appStatus: AppStatus = AppStatus.Install,
    val appStatusRoot: AppStatus = AppStatus.Install,
    val packageName: String? = null,
    val packageNameRoot: String? = null,
    val changelog: String? = null,
    val url: String? = null,
    val versions: List<String>? = null,
    val themes: List<String>? = null,
    val languages: List<String>? = null,
    val downloader: AppDownloader? = null,
    val installationOptions: List<InstallationOption>? = null
)