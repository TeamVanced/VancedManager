package com.vanced.manager.feature.home.domain.entity

data class YouTubeVanced(
    val version: String,
    val versionCode: Long,
    val baseUrl: String,
    val changeLog: String,
    val themes: List<String>,
    val langs: List<String>
)