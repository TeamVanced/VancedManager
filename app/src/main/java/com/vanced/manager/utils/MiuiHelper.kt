package com.vanced.manager.utils

import com.topjohnwu.superuser.Shell

private const val MIUI_PROP = "ro.miui.ui.version.name"

val isMiui get() = !getSystemProperty(MIUI_PROP).isNullOrEmpty()

fun getSystemProperty(propname: String): String? {
    return try {
        Shell.sh("getprop $propname").exec().out.joinToString(" ")
    } catch (e: Exception) {
        null
    }
}