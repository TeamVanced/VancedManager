package com.vanced.manager.feature.home.domain.repository

import com.vanced.manager.feature.home.domain.entity.App

interface AppRepository {

    suspend fun getMicroGInformation(
        packageName: (App.MicroG.Companion) -> String
    ): App.MicroG

    suspend fun getVancedManagerInformation(
        packageName: (App.VancedManager.Companion) -> String
    ): App.VancedManager

    suspend fun getYouTubeMusicVancedInformation(
        packageName: (App.YouTubeMusicVanced.Companion) -> String
    ): App.YouTubeMusicVanced

    suspend fun getYouTubeVancedInformation(
        packageName: (App.YouTubeVanced.Companion) -> String
    ): App.YouTubeVanced
}