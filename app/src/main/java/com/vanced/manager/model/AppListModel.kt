package com.vanced.manager.model

import android.graphics.drawable.Drawable

data class AppListModel(
    val icon: Drawable?,
    val appName: String?,
    val remoteVersion: String?,
    val installedVersion: String?,
    val changelog: String?,
    val pkg: String?
)