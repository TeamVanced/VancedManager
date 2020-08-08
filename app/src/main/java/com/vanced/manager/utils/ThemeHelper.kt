package com.vanced.manager.utils

import android.app.Activity
import android.content.res.Configuration
import androidx.preference.PreferenceManager
import com.vanced.manager.R

object ThemeHelper {

    fun setFinalTheme(activity: Activity) {
        val currentAccent = PreferenceManager.getDefaultSharedPreferences(activity).getString("accent_color", "Blue")
        when (PreferenceManager.getDefaultSharedPreferences(activity)
            .getString("theme_mode", "Follow System")) {
            "Light" -> activity.setTheme(getLightAccent(currentAccent))
            "Dark" -> activity.setTheme(getDarkAccent(currentAccent))
            "Follow System" -> {
                when (activity.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                    Configuration.UI_MODE_NIGHT_YES -> activity.setTheme(getDarkAccent(currentAccent))
                    Configuration.UI_MODE_NIGHT_NO -> activity.setTheme(getLightAccent(currentAccent))
                }
            }
            else -> getLightAccent("Blue")
        }
    }

    private fun getDarkAccent(accentColor: String?): Int {
        return when (accentColor) {
            "Blue" -> R.style.DarkTheme_Blue
            "Red" -> R.style.DarkTheme_Red
            "Green" -> R.style.DarkTheme_Green
            "Yellow" -> R.style.DarkTheme_Yellow
            "Purple" -> R.style.DarkTheme_Purple
            else -> R.style.DarkTheme_Blue
        }
    }

    private fun getLightAccent(accentColor: String?): Int {
        return when (accentColor) {
            "Blue" -> R.style.LightTheme_Blue
            "Red" -> R.style.LightTheme_Red
            "Green" -> R.style.LightTheme_Green
            "Yellow" -> R.style.LightTheme_Yellow
            "Purple" -> R.style.LightTheme_Purple
            else -> R.style.LightTheme_Blue
        }
    }
    
}