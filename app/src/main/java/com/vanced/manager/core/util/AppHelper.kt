package com.vanced.manager.core.util

fun getLatestOrProvidedAppVersion(
    version: String,
    appVersions: List<String>
): String {
    if (appVersions.contains(version))
        return version

    return appVersions.last()
}