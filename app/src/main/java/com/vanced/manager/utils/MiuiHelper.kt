package com.vanced.manager.utils

import android.content.Context
import android.provider.Settings

private const val MIUI_OPTIMIZATION = "miui_optimization"

val Context.isMiuiOptimizationsEnabled: Boolean
    get() = Settings.Secure.getString(
        contentResolver,
        MIUI_OPTIMIZATION
    ) == "1"