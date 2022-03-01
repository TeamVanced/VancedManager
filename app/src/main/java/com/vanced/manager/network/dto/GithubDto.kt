package com.vanced.manager.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GithubReleaseDto(
    @SerialName("tag_name")
    val tagName: String,

    @SerialName("body")
    val body: String,

    @SerialName("assets")
    val assets: List<GithubReleaseAssetDto>
)

@Serializable
data class GithubReleaseAssetDto(
    @SerialName("name")
    val name: String,

    @SerialName("browser_download_url")
    val browserDownloadUrl: String
)