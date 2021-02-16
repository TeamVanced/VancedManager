package com.vanced.manager.utils

import com.topjohnwu.superuser.Shell

private const val MIUI_OPTIMIZATIONS_PROP = "persist.sys.miui_optimization"

val isMiuiOptimizationsEnabled get() = getSystemProperty(MIUI_OPTIMIZATIONS_PROP) == "true"

fun getSystemProperty(propname: String): String? {
    return try {
        Shell.sh("getprop $propname").exec().out.joinToString(" ")
    } catch (e: Exception) {
        null
    }
}