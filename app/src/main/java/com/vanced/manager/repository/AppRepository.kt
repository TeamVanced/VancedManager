package com.vanced.manager.repository

import com.vanced.manager.domain.model.App
import com.vanced.manager.domain.model.AppData
import com.vanced.manager.domain.model.AppState
import com.vanced.manager.domain.model.AppType
import com.vanced.manager.network.GithubService
import com.vanced.manager.network.dto.GithubReleaseDto
import com.vanced.manager.repository.manager.NonrootPackageManager
import com.vanced.manager.repository.manager.PackageManager
import com.vanced.manager.repository.manager.RootPackageManager

interface AppRepository {

    suspend fun getVancedYoutubeNonroot(): App

    suspend fun getVancedYoutubeRoot(): App

    suspend fun getVancedYoutubeMusicNonroot(): App

    suspend fun getVancedYoutubeMusicRoot(): App

    suspend fun getVancedMicrog(): App

    suspend fun getVancedManager(): App

}

class AppRepositoryImpl(
    private val githubService: GithubService,
    private val nonrootPackageManager: NonrootPackageManager,
    private val rootPackageManager: RootPackageManager,
) : AppRepository {

    override suspend fun getVancedYoutubeNonroot(): App {
        val githubRelease = githubService.getVancedYoutubeRelease()
        val remoteVersionCode = githubRelease.getVersionCode()
        val remoteVersionName = githubRelease.getVersionName()
        val installedVersionCode =
            nonrootPackageManager.getVersionCode(AppData.PACKAGE_VANCED_YOUTUBE).getValueOrNull()
        val installedVersionName =
            nonrootPackageManager.getVersionName(AppData.PACKAGE_VANCED_YOUTUBE).getValueOrNull()
        return App(
            name = AppData.NAME_VANCED_YOUTUBE,
            iconResId = AppData.ICON_VANCED_YOUTUBE,
            changelog = githubRelease.body,
            remoteVersionCode = remoteVersionCode,
            remoteVersionName = remoteVersionName,
            installedVersionCode = installedVersionCode,
            installedVersionName = installedVersionName,
            packageName = AppData.PACKAGE_VANCED_YOUTUBE,
            launchActivity = AppData.LAUNCH_ACTIVITY_VANCED_YOUTUBE,
            state = getNoonrotAppState(installedVersionCode, remoteVersionCode),
            app = AppType.VANCED_YOUTUBE,
        )
    }

    override suspend fun getVancedYoutubeRoot(): App {
        val githubRelease = githubService.getVancedYoutubeRelease()
        val remoteVersionCode = githubRelease.getVersionCode()
        val remoteVersionName = githubRelease.getVersionName()
        val installedVersionCode =
            rootPackageManager.getVersionCode(AppData.PACKAGE_ROOT_VANCED_YOUTUBE).getValueOrNull()
        val installedVersionName =
            rootPackageManager.getVersionName(AppData.PACKAGE_ROOT_VANCED_YOUTUBE).getValueOrNull()
        return App(
            name = AppData.NAME_VANCED_YOUTUBE,
            iconResId = AppData.ICON_VANCED_YOUTUBE,
            changelog = githubRelease.body,
            remoteVersionCode = remoteVersionCode,
            remoteVersionName = remoteVersionName,
            installedVersionCode = installedVersionCode,
            installedVersionName = installedVersionName,
            packageName = AppData.PACKAGE_VANCED_YOUTUBE,
            launchActivity = AppData.LAUNCH_ACTIVITY_VANCED_YOUTUBE,
            state = getNoonrotAppState(installedVersionCode, remoteVersionCode),
            app = AppType.VANCED_YOUTUBE,
        )
    }

    override suspend fun getVancedYoutubeMusicNonroot(): App {
        val githubRelease = githubService.getVancedYoutubeMusicRelease()
        val remoteVersionCode = githubRelease.getVersionCode()
        val remoteVersionName = githubRelease.getVersionName()
        val installedVersionCode =
            nonrootPackageManager.getVersionCode(AppData.PACKAGE_VANCED_YOUTUBE_MUSIC).getValueOrNull()
        val installedVersionName =
            nonrootPackageManager.getVersionName(AppData.PACKAGE_VANCED_YOUTUBE_MUSIC).getValueOrNull()
        return App(
            name = AppData.NAME_VANCED_YOUTUBE_MUSIC,
            iconResId = AppData.ICON_VANCED_YOUTUBE_MUSIC,
            changelog = githubRelease.body,
            remoteVersionCode = remoteVersionCode,
            remoteVersionName = remoteVersionName,
            installedVersionCode = installedVersionCode,
            installedVersionName = installedVersionName,
            packageName = AppData.PACKAGE_VANCED_YOUTUBE_MUSIC,
            launchActivity = AppData.LAUNCH_ACTIVITY_VANCED_YOUTUBE_MUSIC,
            state = getNoonrotAppState(installedVersionCode, remoteVersionCode),
            app = AppType.VANCED_YOUTUBE_MUSIC,
        )
    }

    override suspend fun getVancedYoutubeMusicRoot(): App {
        val githubRelease = githubService.getVancedYoutubeMusicRelease()
        val remoteVersionCode = githubRelease.getVersionCode()
        val remoteVersionName = githubRelease.getVersionName()
        val installedVersionCode =
            rootPackageManager.getVersionCode(AppData.PACKAGE_ROOT_VANCED_YOUTUBE_MUSIC).getValueOrNull()
        val installedVersionName =
            rootPackageManager.getVersionName(AppData.PACKAGE_ROOT_VANCED_YOUTUBE_MUSIC).getValueOrNull()
        return App(
            name = AppData.NAME_VANCED_YOUTUBE_MUSIC,
            iconResId = AppData.ICON_VANCED_YOUTUBE_MUSIC,
            changelog = githubRelease.body,
            remoteVersionCode = remoteVersionCode,
            remoteVersionName = remoteVersionName,
            installedVersionCode = installedVersionCode,
            installedVersionName = installedVersionName,
            packageName = AppData.PACKAGE_VANCED_YOUTUBE_MUSIC,
            launchActivity = AppData.LAUNCH_ACTIVITY_VANCED_YOUTUBE_MUSIC,
            state = getNoonrotAppState(installedVersionCode, remoteVersionCode),
            app = AppType.VANCED_YOUTUBE_MUSIC,
        )
    }

    override suspend fun getVancedMicrog(): App {
        val githubRelease = githubService.getVancedMicrogRelease()
        val remoteVersionCode = githubRelease.getVersionCode()
        val remoteVersionName = githubRelease.getVersionName()
        val installedVersionCode =
            nonrootPackageManager.getVersionCode(AppData.PACKAGE_VANCED_MICROG).getValueOrNull()
        val installedVersionName =
            nonrootPackageManager.getVersionName(AppData.PACKAGE_VANCED_MICROG).getValueOrNull()
        return App(
            name = AppData.NAME_VANCED_MICROG,
            iconResId = AppData.ICON_VANCED_MICROG,
            changelog = githubRelease.body,
            remoteVersionCode = remoteVersionCode,
            remoteVersionName = remoteVersionName,
            installedVersionCode = installedVersionCode,
            installedVersionName = installedVersionName,
            packageName = AppData.PACKAGE_VANCED_MICROG,
            launchActivity = AppData.LAUNCH_ACTIVITY_VANCED_MICROG,
            state = getNoonrotAppState(installedVersionCode, remoteVersionCode),
            app = AppType.VANCED_MICROG,
        )
    }

    override suspend fun getVancedManager(): App {
        val githubRelease = githubService.getVancedManagerRelease()
        val remoteVersionCode = githubRelease.getVersionCode()
        val remoteVersionName = githubRelease.getVersionName()
        val installedVersionCode =
            nonrootPackageManager.getVersionCode(AppData.PACKAGE_VANCED_MANAGER).getValueOrNull()
        val installedVersionName =
            nonrootPackageManager.getVersionName(AppData.PACKAGE_VANCED_MANAGER).getValueOrNull()
        return App(
            name = AppData.NAME_VANCED_MANAGER,
            iconResId = AppData.ICON_VANCED_MANAGER,
            changelog = githubRelease.body,
            remoteVersionCode = remoteVersionCode,
            remoteVersionName = remoteVersionName,
            installedVersionCode = installedVersionCode,
            installedVersionName = installedVersionName,
            packageName = AppData.PACKAGE_VANCED_MANAGER,
            launchActivity = AppData.LAUNCH_ACTIVITY_VANCED_MANAGER,
            state = getNoonrotAppState(installedVersionCode, remoteVersionCode),
            app = AppType.VANCED_MANAGER,
        )
    }

    private fun getNoonrotAppState(
        installedVersionCode: Int?,
        remoteVersionCode: Int
    ): AppState {
        return when {
            installedVersionCode == null -> AppState.NOT_INSTALLED
            installedVersionCode < remoteVersionCode -> AppState.NEEDS_UPDATE
            installedVersionCode >= remoteVersionCode -> AppState.INSTALLED
            else -> AppState.NOT_INSTALLED
        }
    }

    private fun GithubReleaseDto.getVersionCode() = tagName.substringAfter("-").toInt()
    private fun GithubReleaseDto.getVersionName() = tagName.substringBefore("-")
}