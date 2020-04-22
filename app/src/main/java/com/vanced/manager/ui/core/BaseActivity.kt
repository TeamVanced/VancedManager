package com.vanced.manager.ui.core

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.preference.PreferenceManager
import com.vanced.manager.R

@SuppressLint("Registered")
open class ThemeActivity : AppCompatActivity() {
    private lateinit var currentTheme: String
    private lateinit var pref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        pref = PreferenceManager.getDefaultSharedPreferences(this)
        currentTheme = pref.getString("theme_mode", "").toString()

        setFinalTheme(currentTheme)
        super.onCreate(savedInstanceState)

        if (android.os.Build.VERSION.SDK_INT < 28) {
            setTaskBG()
        }
    }

    override fun onResume() {
        val theme = pref.getString("theme_mode", "")
        super.onResume()
        if (currentTheme != theme)
            recreate()
        if (android.os.Build.VERSION.SDK_INT < 28) {
            setTaskBG()
        }
    }
    private fun setFinalTheme(currentTheme: String) {
        when (currentTheme) {
            "LIGHT" -> setTheme(R.style.LightTheme_Blue)
            "DARK" -> setTheme(R.style.DarkTheme_Blue)
            "FOLLOW" -> {
                when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                    Configuration.UI_MODE_NIGHT_YES -> setTheme(R.style.DarkTheme_Blue)
                    Configuration.UI_MODE_NIGHT_NO -> setTheme(R.style.LightTheme_Blue)
                }
            }
            else -> setTheme(R.style.LightTheme_Blue)
        }
    }
    private fun setTaskBG() {
        val label = getString(R.string.app_name)
        val color = ResourcesCompat.getColor(resources, R.color.Black, null)
        val taskDec: ActivityManager.TaskDescription = ActivityManager.TaskDescription(label, null, color)
        setTaskDescription(taskDec)
    }
}