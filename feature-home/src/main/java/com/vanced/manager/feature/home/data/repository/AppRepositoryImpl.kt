package com.vanced.manager.feature.home.data.repository

import android.content.pm.PackageManager
import com.vanced.manager.feature.home.data.datasource.AppInformationRemoteDataSource
import com.vanced.manager.feature.home.data.datasource.PkgInformationDataSource
import com.vanced.manager.feature.home.domain.entity.App
import com.vanced.manager.feature.home.domain.entity.AppState
import com.vanced.manager.feature.home.domain.repository.AppRepository

class AppRepositoryImpl(
    private val remoteDtaSource: AppInformationRemoteDataSource,
    private val localDataSource: PkgInformationDataSource
) : AppRepository {

    override suspend fun getMicroGInformation(
        packageName: (App.MicroG.Companion) -> String
    ): App.MicroG {
        val pkg = packageName(App.MicroG.Companion)
        val remoteData = remoteDtaSource.getMicroGInformation()
        val localCode = tryLocalCode(pkg)
        return App.MicroG(
            remoteInfo = remoteData,
            localVersionCode = localCode,
            localVersionName = tryLocalName(pkg),
            state = compareCode(remoteData.versionCode, localCode)
        )
    }

    override suspend fun getVancedManagerInformation(
        packageName: (App.VancedManager.Companion) -> String
    ): App.VancedManager {
        val pkg = packageName(App.VancedManager.Companion)
        val remoteData = remoteDtaSource.getVancedManagerInformation()
        val localCode = tryLocalCode(pkg)
        return App.VancedManager(
            remoteInfo = remoteData,
            localVersionCode = localCode,
            localVersionName = tryLocalName(pkg),
            state = compareCode(remoteData.versionCode, localCode)
        )
    }

    override suspend fun getYouTubeMusicVancedInformation(
        packageName: (App.YouTubeMusicVanced.Companion) -> String
    ): App.YouTubeMusicVanced {
        val pkg = packageName(App.YouTubeMusicVanced.Companion)
        val remoteData = remoteDtaSource.getYouTubeMusicVancedInformation()
        val localCode = tryLocalCode(pkg)
        return App.YouTubeMusicVanced(
            remoteInfo = remoteData,
            localVersionCode = localCode,
            localVersionName = tryLocalName(pkg),
            state = compareCode(remoteData.versionCode, localCode)
        )
    }

    override suspend fun getYouTubeVancedInformation(
        packageName: (App.YouTubeVanced.Companion) -> String
    ): App.YouTubeVanced {
        val pkg = packageName(App.YouTubeVanced.Companion)
        val remoteData = remoteDtaSource.getYouTubeVancedInformation()
        val localCode = tryLocalCode(pkg)
        return App.YouTubeVanced(
            remoteInfo = remoteData,
            localVersionCode = localCode,
            localVersionName = tryLocalName(pkg),
            state = compareCode(remoteData.versionCode, localCode)
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

    private suspend fun tryLocalCode(packageName: String): Int? =
        try {
            localDataSource.getVersionCode(packageName)
        } catch (exception: PackageManager.NameNotFoundException) {
            null
        }

    private suspend fun tryLocalName(packageName: String): String? =
        try {
            localDataSource.getVersionName(packageName)
        } catch (exception: PackageManager.NameNotFoundException) {
            null
        }
}