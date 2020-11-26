package com.vanced.manager.feature.home.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.vanced.manager.feature.home.domain.entity.VancedManager

@JsonClass(generateAdapter = true)
data class VancedManagerDto(
    @Json(name = "version") val version: String,
    @Json(name = "versionCode") val versionCode: Long,
    @Json(name = "url") val baseUrl: String,
    @Json(name = "changelog") val changeLog: String
)

fun VancedManagerDto.toEntity() =
    VancedManager(version, versionCode, baseUrl, changeLog)

fun VancedManager.toDto() =
    VancedManagerDto(version, versionCode, baseUrl, changeLog)