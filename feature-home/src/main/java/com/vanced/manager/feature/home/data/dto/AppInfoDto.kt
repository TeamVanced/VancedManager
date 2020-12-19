package com.vanced.manager.feature.home.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AppInfoDto(
    @Json(name = "name") val name: String,
    @Json(name = "iconUrl") val iconUrl: String,
    @Json(name = "versionName") val versionName: String,
    @Json(name = "versionCode") val versionCode: Int,
    @Json(name = "pkgName") val pkgName: String,
    @Json(name = "downloadUrl") val downloadUrl: String,
    @Json(name = "changelog") val changelog: String,
    @Json(name = "languages") val languages: List<String>?,
    @Json(name = "themes") val themes: List<String>?,
)
