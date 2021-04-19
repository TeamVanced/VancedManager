package com.vanced.manager.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

val Context.installPrefs: SharedPreferences
    get() = getSharedPreferences(
        "installPrefs",
        Context.MODE_PRIVATE
    )

var SharedPreferences.lang
    get() = getString("lang", getDefaultVancedLanguages())
    set(value) = edit { putString("lang", value) }


var SharedPreferences.theme
    get() = getString("theme", "dark")
    set(value) = edit { putString("theme", value) }