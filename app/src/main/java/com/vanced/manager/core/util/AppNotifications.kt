package com.vanced.manager.core.util

import com.vanced.manager.domain.model.NotificationPrefModel
import com.vanced.manager.network.util.MICROG_NAME
import com.vanced.manager.network.util.MUSIC_NAME
import com.vanced.manager.network.util.VANCED_NAME

val notificationApps = arrayOf(
    NotificationPrefModel(
        app = VANCED_NAME,
        prefKey = "vanced"
    ),
    NotificationPrefModel(
        app = MUSIC_NAME,
        prefKey = "music"
    ),
    NotificationPrefModel(
        app = MICROG_NAME,
        prefKey = "microg"
    )
)