package com.vanced.manager.feature.home.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.vanced.manager.feature.home.domain.entity.VancedManagerInfo

@JsonClass(generateAdapter = true)
data class VancedManagerDto(
    @Json(name = "version") val version: String,
    @Json(name = "versionCode") val versionCode: Int,
    @Json(name = "url") val baseUrl: String,
    @Json(name = "changelog") val changeLog: String
)

fun VancedManagerDto.toEntity() =
    VancedManagerInfo(version, versionCode, baseUrl, changeLog)

fun VancedManagerInfo.toDto() =
    VancedManagerDto(version, versionCode, baseUrl, changeLog)