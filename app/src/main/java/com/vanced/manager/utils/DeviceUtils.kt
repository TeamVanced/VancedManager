package com.vanced.manager.utils

import android.os.Build

fun getArch(): String = when {
    Build.SUPPORTED_ABIS.contains("x86") -> "x86"
    Build.SUPPORTED_ABIS.contains("arm64-v8a") -> "arm64_v8a"
    else -> "armeabi_v7a"
}