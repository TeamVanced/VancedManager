package com.vanced.manager.domain.model

import androidx.annotation.DrawableRes
import com.vanced.manager.R

data class App(
    val name: String,
    @DrawableRes val iconResId: Int,
    val changelog: String,
    val remoteVersionCode: Int,
    val remoteVersionName: String,
    val installedVersionCode: Int?,
    val installedVersionName: String?,
    val packageName: String,
    val launchActivity: String,
    val state: AppState,
    val app: AppType
)

object AppData {
    const val NAME_VANCED_YOUTUBE = "YouTube Vanced"
    const val NAME_VANCED_YOUTUBE_MUSIC = "YouTube Vanced Music"
    const val NAME_VANCED_MICROG = "Vanced microG"
    const val NAME_VANCED_MANAGER = "Vanced Manager"

    const val ICON_VANCED_YOUTUBE = R.drawable.ic_vanced
    const val ICON_VANCED_YOUTUBE_MUSIC = R.drawable.ic_music
    const val ICON_VANCED_MICROG = R.drawable.ic_microg
    const val ICON_VANCED_MANAGER = R.drawable.ic_manager

    const val PACKAGE_VANCED_YOUTUBE = "com.vanced.android.youtube"
    const val PACKAGE_VANCED_YOUTUBE_MUSIC = "com.vanced.android.youtube.apps.music"
    const val PACKAGE_VANCED_MICROG = "com.mgoogle.android.gms"
    const val PACKAGE_VANCED_MANAGER = "com.vanced.manager"

    const val PACKAGE_ROOT_VANCED_YOUTUBE = "com.google.android.youtube"
    const val PACKAGE_ROOT_VANCED_YOUTUBE_MUSIC = "com.google.android.youtube.apps.music"

    const val LAUNCH_ACTIVITY_VANCED_YOUTUBE = "com.google.android.youtube.HomeActivity"
    const val LAUNCH_ACTIVITY_VANCED_YOUTUBE_MUSIC =
        "com.google.android.apps.youtube.music.activities.MusicActivity"
    const val LAUNCH_ACTIVITY_VANCED_MICROG = "org.microg.gms.ui.SettingsActivity"
    const val LAUNCH_ACTIVITY_VANCED_MANAGER = ""
}

enum class AppType {
    VANCED_YOUTUBE,
    VANCED_YOUTUBE_MUSIC,
    VANCED_MICROG,
    VANCED_MANAGER,
}

enum class AppState {
    NOT_INSTALLED,
    INSTALLED,
    NEEDS_UPDATE
}


