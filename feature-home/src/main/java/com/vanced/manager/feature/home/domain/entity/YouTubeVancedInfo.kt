package com.vanced.manager.feature.home.domain.entity

data class YouTubeVancedInfo(
    val version: String,
    val versionCode: Int,
    val baseUrl: String,
    val changeLog: String,
    val themes: List<String>,
    val langs: List<String>
)