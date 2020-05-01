package com.vanced.manager.ui.core

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.preference.PreferenceManager
import com.vanced.manager.R
import java.util.*

// This activity will NOT be used in manifest
// since MainActivity will extend it
@SuppressLint("Registered")
open class ThemeActivity : AppCompatActivity() {

    private lateinit var currentTheme: String
    private lateinit var pref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        pref = PreferenceManager.getDefaultSharedPreferences(this)
        currentTheme = pref.getString("theme_mode", "").toString()

        setFinalTheme(currentTheme)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            setTaskBG()
        }
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        val theme = pref.getString("theme_mode", "")

        //if for some weird reasons we get invalid
        //theme, recreate activity
        if (currentTheme != theme)
            recreate()

        //set Task Header color in recents menu for
        //devices with lower Android version than Pie
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            setTaskBG()
        }
        super.onResume()
    }

    //This stupid ass AppCompatDelegate does
    //not want to work, so I have to use my
    //own implementation of theme switching
    open fun setFinalTheme(currentTheme: String) {
        when (currentTheme) {
            "LIGHT" -> setTheme(R.style.MainTheme_Light)
            "DARK" -> setTheme(R.style.MainTheme_Dark)
            "FOLLOW" -> {
                when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                    Configuration.UI_MODE_NIGHT_YES -> setTheme(R.style.MainTheme_Light)
                    Configuration.UI_MODE_NIGHT_NO -> setTheme(R.style.MainTheme_Dark)
                }
            }
            else -> setTheme(R.style.MainTheme_Light)
        }
    }

    fun setTaskBG() {
        val label = getString(R.string.app_name)
        val color = ResourcesCompat.getColor(resources, R.color.Black, null)
        val taskDec: ActivityManager.TaskDescription =
            ActivityManager.TaskDescription(label, null, color)
        setTaskDescription(taskDec)

    }

}