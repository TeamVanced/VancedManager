package com.vanced.manager.feature.home.domain.entity

import android.graphics.Bitmap

data class AppInfo(
    val name: String,
    val icon: Bitmap,
    val versionName: String,
    val versionCode: Int,
    val pkgName: String,
    val downloadUrl: String,
    val changelog: String,
    val languages: List<String>?,
    val themes: List<String>?,
    val localVersionName: String?,
    val localVersionCode: Int?,
    val state: AppState,
)