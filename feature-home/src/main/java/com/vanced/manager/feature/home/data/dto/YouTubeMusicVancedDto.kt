package com.vanced.manager.feature.home.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.vanced.manager.feature.home.domain.entity.YouTubeMusicVancedInfo

@JsonClass(generateAdapter = true)
data class YouTubeMusicVancedDto(
    @Json(name = "version") val version: String,
    @Json(name = "versionCode") val versionCode: Int,
    @Json(name = "url") val baseUrl: String,
    @Json(name = "changelog") val changeLog: String
)

fun YouTubeMusicVancedDto.toEntity() =
    YouTubeMusicVancedInfo(version, versionCode, baseUrl, changeLog)

fun YouTubeMusicVancedInfo.toDto() =
    YouTubeMusicVancedDto(version, versionCode, baseUrl, changeLog)