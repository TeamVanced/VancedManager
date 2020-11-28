package com.vanced.manager.feature.home.domain.entity

data class VancedManagerInfo(
    val version: String,
    val versionCode: Int,
    val baseUrl: String,
    val changeLog: String
)