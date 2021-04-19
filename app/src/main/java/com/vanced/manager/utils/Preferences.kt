package com.vanced.manager.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager.getDefaultSharedPreferences

val Context.defPrefs: SharedPreferences get() = getDefaultSharedPreferences(this)

var SharedPreferences.managerTheme
    get() = getString("manager_theme", "System Default")
    set(value) = edit { putString("manager_theme", value) }

var SharedPreferences.managerAccent
    get() = getInt("manager_accent_color", defAccentColor)
    set(value) = edit { putInt("manager_accent_color", value) }

var SharedPreferences.managerVariant
    get() = getString("vanced_variant", "nonroot")
    set(value) = edit { putString("vanced_variant", value) }

var SharedPreferences.managerLang
    get() = getString("manager_lang", "System Default")
    set(value) = edit { putString("manager_lang", value) }

var SharedPreferences.installUrl
    get() = getString("install_url", baseUrl)
    set(value) = edit { putString("install_url", value) }

var SharedPreferences.vancedVersion
    get() = getString("vanced_version", "latest")
    set(value) = edit { putString("vanced_version", value) }

var SharedPreferences.musicVersion
    get() = getString("music_version", "latest")
    set(value) = edit { putString("music_version", value) }

var SharedPreferences.serviceDSleepTimer
    get() = getInt("serviced_sleep_timer", 1)
    set(value) = edit { putInt("serviced_sleep_timer", value) }

var SharedPreferences.enableVanced
    get() = getBoolean("enable_vanced", true)
    set(value) = edit { putBoolean("enable_vanced", value) }

var SharedPreferences.enableMusic
    get() = getBoolean("enable_music", true)
    set(value) = edit { putBoolean("enable_music", value) }

