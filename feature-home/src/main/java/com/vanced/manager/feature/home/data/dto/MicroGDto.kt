package com.vanced.manager.feature.home.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.vanced.manager.feature.home.domain.entity.MicroGInfo

@JsonClass(generateAdapter = true)
data class MicroGDto(
    @Json(name = "version") val version: String,
    @Json(name = "versionCode") val versionCode: Int,
    @Json(name = "url") val baseUrl: String,
    @Json(name = "changelog") val changeLog: String
)

fun MicroGDto.toEntity() =
    MicroGInfo(version, versionCode, baseUrl, changeLog)

fun MicroGInfo.toDto() =
    MicroGDto(version, versionCode, baseUrl, changeLog)