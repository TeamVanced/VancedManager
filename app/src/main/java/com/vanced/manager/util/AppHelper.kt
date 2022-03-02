package com.vanced.manager.util

fun getLatestOrProvidedAppVersion(
    version: String,
    appVersions: List<String>?
): String {
    if (appVersions == null)
        return version

    if (appVersions.contains(version))
        return version

    return appVersions.last()
}