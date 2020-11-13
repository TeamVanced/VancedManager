package com.vanced.manager.utils

import android.app.Activity
import android.content.res.Configuration
import com.vanced.manager.R
import com.vanced.manager.utils.Extensions.getDefaultPrefs

object ThemeHelper {

    fun Activity.setFinalTheme() {
        val prefs = getDefaultPrefs()
        val currentAccent = prefs.getString("manager_accent", "Blue")
        when (prefs.getString("manager_theme", "System Default")) {
            "Light" -> setTheme(getLightAccent(currentAccent))
            "Dark" -> setTheme(getDarkAccent(currentAccent))
            "System Default" -> {
                when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                    Configuration.UI_MODE_NIGHT_YES -> setTheme(getDarkAccent(currentAccent))
                    Configuration.UI_MODE_NIGHT_NO -> setTheme(getLightAccent(currentAccent))
                }
            }
            else -> setTheme(getLightAccent("Blue"))
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