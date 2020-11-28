package com.vanced.manager.feature.home.data.repository

import com.vanced.manager.feature.home.data.datasource.AppIconDataSource
import com.vanced.manager.feature.home.data.datasource.AppsInfoDataSource
import com.vanced.manager.feature.home.data.datasource.PkgInformationDataSource
import com.vanced.manager.feature.home.data.dto.AppInfoDto
import com.vanced.manager.feature.home.domain.entity.AppInfo
import com.vanced.manager.feature.home.domain.entity.AppState
import com.vanced.manager.feature.home.domain.repository.AppsRepository


class AppsRepositoryImpl(
	private val appsInfoDataSource: AppsInfoDataSource,
	private val appIconDataSource: AppIconDataSource,
	private val pkgInformationDataSource: PkgInformationDataSource,
	private val channel: String //TODO experiment
) : AppsRepository {

    // TODO switch content OR send toggles to build retrofit and change channel get json
    override suspend fun getAppsInfo(): List<AppInfo> =
        appsInfoDataSource.getAppsInfo(channel)
            .map { it.toEntity() }

    private suspend fun AppInfoDto.toEntity(): AppInfo {
        val localVersionName = pkgInformationDataSource.getVersionName(pkgName)
        val localVersionCode = pkgInformationDataSource.getVersionCode(pkgName)
        return AppInfo(
			name = name,
			icon = appIconDataSource.getIcon(iconUrl),
			versionName = versionName,
			versionCode = versionCode,
			pkgName = pkgName,
			downloadUrl = downloadUrl,
			changelog = changelog,
			languages = languages,
			themes = themes,
			localVersionName = localVersionName,
			localVersionCode = localVersionCode,
			state = compareCode(versionCode, localVersionCode)
		)
    }

    private fun compareCode(remote: Int, local: Int?): AppState =
        if (local != null) {
            when {
                remote > local -> AppState.UPDATE
                remote == local -> AppState.REINSTALL
                remote < local -> AppState.REINSTALL
                else -> throw IllegalArgumentException("Unknown versions")
            }
        } else {
            AppState.INSTALL
        }
}