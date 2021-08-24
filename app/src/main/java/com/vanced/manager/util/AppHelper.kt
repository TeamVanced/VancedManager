package com.vanced.manager.util

import com.vanced.manager.domain.model.App

fun getLatestOrProvidedAppVersion(
    version: String,
    app: App
): String {
    if (version == "latest") {
        return app.versions?.last() ?: version
    }
    return version
}