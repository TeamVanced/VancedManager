package com.vanced.manager.feature.home.domain.entity

data class VancedManager(
    val version: String,
    val versionCode: Long,
    val baseUrl: String,
    val changeLog: String
)