package com.vanced.manager.feature.home.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.vanced.manager.feature.home.domain.entity.MicroG

@JsonClass(generateAdapter = true)
data class MicroGDto(
    @Json(name = "version") val version: String,
    @Json(name = "versionCode") val versionCode: Long,
    @Json(name = "url") val baseUrl: String,
    @Json(name = "changelog") val changeLog: String
)

fun MicroGDto.toEntity() =
    MicroG(version, versionCode, baseUrl, changeLog)

fun MicroG.toDto() =
    MicroGDto(version, versionCode, baseUrl, changeLog)