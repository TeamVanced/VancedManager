package com.vanced.manager.network.model

import com.vanced.manager.domain.datasource.PackageInformationDataSource
import com.vanced.manager.domain.model.App
import com.vanced.manager.domain.model.AppStatus
import com.vanced.manager.domain.util.EntityMapper
import com.vanced.manager.downloader.base.BaseDownloader
import com.vanced.manager.downloader.impl.MicrogDownloader
import com.vanced.manager.downloader.impl.MusicDownloader
import com.vanced.manager.downloader.impl.VancedDownloader
import com.vanced.manager.network.util.MICROG_NAME
import com.vanced.manager.network.util.MUSIC_NAME
import com.vanced.manager.network.util.VANCED_NAME

class AppDtoMapper(
    private val packageInformationDataSource: PackageInformationDataSource,
    private val vancedDownloader: VancedDownloader
) : EntityMapper<AppDto, App> {

    override suspend fun mapToModel(entity: AppDto): App =
        with (entity) {
            val localVersionCode = packageInformationDataSource.getVersionCode(packageName ?: "")
            val localVersionCodeRoot = packageInformationDataSource.getVersionCode(packageNameRoot ?: "")
            val localVersionName = packageInformationDataSource.getVersionName(packageName ?: "")
            val localVersionNameRoot = packageInformationDataSource.getVersionName(packageNameRoot ?: "")
            App(
                name = name,
                remoteVersion = version,
                remoteVersionCode = versionCode,
                installedVersion = localVersionName,
                installedVersionCode = localVersionCode,
                installedVersionRoot = localVersionNameRoot,
                installedVersionCodeRoot = localVersionCodeRoot,
                appStatus = compareVersionCodes(versionCode, localVersionCode),
                appStatusRoot = compareVersionCodes(versionCode, localVersionCodeRoot),
                packageName = packageName,
                packageNameRoot = packageNameRoot,
                iconUrl = iconUrl,
                changelog = changelog,
                url = url,
                themes = themes,
                languages = languages,
                downloader = getDownloader(name)
            )
        }

    override suspend fun mapFromModel(model: App): AppDto =
        with (model) {
            AppDto(
                name = name,
                version = remoteVersion,
                versionCode = remoteVersionCode,
                changelog = changelog,
                url = url,
                themes = themes,
                languages = languages,
                packageName = packageName,
                packageNameRoot = packageNameRoot,
                iconUrl = iconUrl
            )
        }

    private fun compareVersionCodes(remote: Int?, local: Int?): AppStatus =
        if (local != null && remote != null) {
            when {
                remote > local -> AppStatus.Update
                remote <= local -> AppStatus.Reinstall
                else -> AppStatus.Install
            }
        } else {
            AppStatus.Install
        }

    private fun getDownloader(app: String?): BaseDownloader? =
        when (app) {
            VANCED_NAME -> vancedDownloader
            MUSIC_NAME -> MusicDownloader
            MICROG_NAME -> MicrogDownloader
            else -> null
        }
}