package com.vanced.manager.network

import com.vanced.manager.network.dto.GithubReleaseDto
import retrofit2.http.GET

private const val REPOS_VANCED = "repos/YTVanced"

interface GithubService {

    @GET("$REPOS_VANCED/Vanced/releases/latest")
    suspend fun getVancedYoutubeRelease(): GithubReleaseDto

    @GET("$REPOS_VANCED/VancedMusic/releases/latest")
    suspend fun getVancedYoutubeMusicRelease(): GithubReleaseDto

    @GET("$REPOS_VANCED/VancedMicrog/releases/latest")
    suspend fun getVancedMicrogRelease(): GithubReleaseDto

    @GET("$REPOS_VANCED/VancedManager/releases/latest")
    suspend fun getVancedManagerRelease(): GithubReleaseDto

}