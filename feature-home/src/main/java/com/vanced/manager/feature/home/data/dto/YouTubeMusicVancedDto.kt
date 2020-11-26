package com.vanced.manager.feature.home.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.vanced.manager.feature.home.domain.entity.YouTubeMusicVanced

@JsonClass(generateAdapter = true)
data class YouTubeMusicVancedDto(
    @Json(name = "version") val version: String,
    @Json(name = "versionCode") val versionCode: Long,
    @Json(name = "url") val baseUrl: String,
    @Json(name = "changelog") val changeLog: String
)

fun YouTubeMusicVancedDto.toEntity() =
    YouTubeMusicVanced(version, versionCode, baseUrl, changeLog)

fun YouTubeMusicVanced.toDto() =
    YouTubeMusicVancedDto(version, versionCode, baseUrl, changeLog)