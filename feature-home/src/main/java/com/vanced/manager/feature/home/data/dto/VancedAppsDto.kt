package com.vanced.manager.feature.home.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VancedAppsDto(
    @Json(name = "manager") val vancedManager: VancedManagerDto,
    @Json(name = "vanced") val youTubeVanced: YouTubeVancedDto,
    @Json(name = "music") val youTubeMusicVanced: YouTubeMusicVancedDto,
    @Json(name = "microg") val microG: MicroGDto
)