package com.vanced.manager.feature.home.domain.entity


sealed class App<R>(
    open val remoteInfo: R,
    open val localVersionCode: Int?,
    open val localVersionName: String?,
    open val state: AppState
) {
    data class MicroG(
        override val remoteInfo: MicroGInfo,
        override val localVersionCode: Int?,
        override val localVersionName: String?,
        override val state: AppState
    ) : App<MicroGInfo>(remoteInfo, localVersionCode, localVersionName, state) {
        companion object {
            const val PKG_NAME = "com.mgoogle.android.gms"
        }
    }

    data class VancedManager(
        override val remoteInfo: VancedManagerInfo,
        override val localVersionCode: Int?,
        override val localVersionName: String?,
        override val state: AppState
    ) : App<VancedManagerInfo>(remoteInfo, localVersionCode, localVersionName, state) {
        companion object {
            const val PKG_NAME = "com.vanced.manager" // TODO replace
        }
    }

    data class YouTubeMusicVanced(
        override val remoteInfo: YouTubeMusicVancedInfo,
        override val localVersionCode: Int?,
        override val localVersionName: String?,
        override val state: AppState
    ) : App<YouTubeMusicVancedInfo>(remoteInfo, localVersionCode, localVersionName, state) {
        companion object {
            const val PKG_NAME = "com.vanced.android.apps.youtube.music"
            const val PKG_NAME_ROOT = "com.google.android.apps.youtube.music"
        }
    }

    data class YouTubeVanced(
        override val remoteInfo: YouTubeVancedInfo,
        override val localVersionCode: Int?,
        override val localVersionName: String?,
        override val state: AppState
    ) : App<YouTubeVancedInfo>(remoteInfo, localVersionCode, localVersionName, state) {
        companion object {
            const val PKG_NAME = "com.vanced.android.youtube"
            const val PKG_NAME_ROOT = "com.google.android.youtube"
        }
    }
}