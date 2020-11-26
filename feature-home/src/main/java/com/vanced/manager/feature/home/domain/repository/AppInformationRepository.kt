package com.vanced.manager.feature.home.domain.repository

import com.vanced.manager.feature.home.domain.entity.*

interface AppInformationRepository {

    suspend fun getAppInformation(): VancedApps

    suspend fun getMicroGInformation(): MicroG

    suspend fun getVancedManagerInformation(): VancedManager

    suspend fun getYouTubeMusicVancedInformation(): YouTubeMusicVanced

    suspend fun getYouTubeVancedInformation(): YouTubeVanced
}