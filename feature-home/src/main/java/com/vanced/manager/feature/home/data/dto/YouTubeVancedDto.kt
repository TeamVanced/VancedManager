package com.vanced.manager.feature.home.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.vanced.manager.feature.home.domain.entity.YouTubeVanced

@JsonClass(generateAdapter = true)
data class YouTubeVancedDto(
    @Json(name = "version") val version: String,
    @Json(name = "versionCode") val versionCode: Long,
    @Json(name = "url") val baseUrl: String,
    @Json(name = "changelog") val changeLog: String,
    @Json(name = "themes") val themes: List<String>,
    @Json(name = "langs") val langs: List<String>
)


fun YouTubeVancedDto.toEntity() =
    YouTubeVanced(version, versionCode, baseUrl, changeLog, themes, langs)

fun YouTubeVanced.toDto() =
    YouTubeVancedDto(version, versionCode, baseUrl, changeLog, themes, langs)