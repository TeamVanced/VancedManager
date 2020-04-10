package com.vanced.manager.ui.core

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.vanced.manager.R

@SuppressLint("Registered")
open class ThemeActivity : AppCompatActivity() {
    private lateinit var currentTheme: String
    private lateinit var pref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        pref = PreferenceManager.getDefaultSharedPreferences(this)
        currentTheme = pref.getString("theme_mode", "").toString()

        setFinalTheme(currentTheme)

    }

    override fun onResume() {
        super.onResume()
        val theme = pref.getString("theme_mode", "")
        if (currentTheme != theme)
            recreate()
    }
    private fun setFinalTheme(currentTheme: String) {
        when (currentTheme) {
            "LIGHT" -> setTheme(R.style.LightTheme_Blue)
            "DARK" -> setTheme(R.style.DarkTheme_Blue)
            "FOLLOW" -> {
                when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                    Configuration.UI_MODE_NIGHT_YES -> setTheme(R.style.DarkTheme_Blue)
                    Configuration.UI_MODE_NIGHT_NO -> setTheme(R.style.LightTheme_Blue)
                    else -> setTheme(R.style.LightTheme_Blue)
                }
            }
            else -> setTheme(R.style.LightTheme_Blue)
        }
    }
}