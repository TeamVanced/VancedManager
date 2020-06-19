package com.vanced.manager.utils

import android.content.Context
import android.content.res.Configuration
import androidx.preference.PreferenceManager
import com.vanced.manager.R

object ThemeHelper {

    fun setFinalTheme(context: Context) {
        val currentAccent = PreferenceManager.getDefaultSharedPreferences(context).getString("accent_color", "Blue")
        when (PreferenceManager.getDefaultSharedPreferences(context).getString("theme_mode", "Blue")) {
            "Light" -> setLightAccent(currentAccent, context)
            "Dark" -> setDarkAccent(currentAccent, context)
            "Follow System" -> {
                when (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                    Configuration.UI_MODE_NIGHT_YES -> setDarkAccent(currentAccent, context)
                    Configuration.UI_MODE_NIGHT_NO -> setLightAccent(currentAccent, context)
                }
            }
            else -> setLightAccent("Blue", context)
        }
    }

    private fun setDarkAccent(accentColor: String?, context: Context) {
        when (accentColor) {
            "Blue" -> context.setTheme(R.style.DarkTheme_Blue)
            "Red" -> context.setTheme(R.style.DarkTheme_Red)
            "Green" -> context.setTheme(R.style.DarkTheme_Green)
            "Yellow" -> context.setTheme(R.style.DarkTheme_Yellow)
            "Purple" -> context.setTheme(R.style.DarkTheme_Purple)
            else -> context.setTheme(R.style.DarkTheme_Blue)
        }
    }

    private fun setLightAccent(accentColor: String?, context: Context) {
        when (accentColor) {
            "Blue" -> context.setTheme(R.style.LightTheme_Blue)
            "Red" -> context.setTheme(R.style.LightTheme_Red)
            "Green" -> context.setTheme(R.style.LightTheme_Green)
            "Yellow" -> context.setTheme(R.style.LightTheme_Yellow)
            "Purple" -> context.setTheme(R.style.LightTheme_Purple)
            else -> context.setTheme(R.style.LightTheme_Blue)
        }
    }
    
}