package com.vanced.manager.preference

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager

val Context.defPrefs: SharedPreferences get() = PreferenceManager.getDefaultSharedPreferences(this)

var SharedPreferences.managerTheme
    get() = getString("manager_theme", "Light")
    set(value) {
        edit {
            putString("manager_theme", value)
        }
    }