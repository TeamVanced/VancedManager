package com.vanced.manager.feature.home.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.vanced.manager.feature.home.domain.entity.VancedApps

@JsonClass(generateAdapter = true)
data class VancedAppsDto(
    @Json(name = "manager") val vancedManager: VancedManagerDto,
    @Json(name = "vanced") val youTubeVanced: YouTubeVancedDto,
    @Json(name = "music") val youTubeMusicVanced: YouTubeMusicVancedDto,
    @Json(name = "microg") val microG: MicroGDto
)


fun VancedAppsDto.toEntity() =
    VancedApps(
        vancedManager = vancedManager.toEntity(),
        youTubeVanced = youTubeVanced.toEntity(),
        youTubeMusicVanced = youTubeMusicVanced.toEntity(),
        microG = microG.toEntity()
    )

fun VancedApps.toDto() =
    VancedAppsDto(
        vancedManager = vancedManager.toDto(),
        youTubeVanced = youTubeVanced.toDto(),
        youTubeMusicVanced = youTubeMusicVanced.toDto(),
        microG = microG.toDto()
    )