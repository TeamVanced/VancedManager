package com.vanced.manager.utils

import android.app.Activity
import android.content.res.Configuration
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vanced.manager.R

const val defAccentColor: Int = -13732865
const val LIGHT = "Light"
const val DARK = "Dark"
const val SYSTEM_DEFAULT = "System Default"

val mutableAccentColor = MutableLiveData(defAccentColor)
val accentColor: LiveData<Int> = mutableAccentColor

var currentTheme = ""

fun Activity.setFinalTheme() {
    when (defPrefs.managerTheme) {
        LIGHT -> setTheme(R.style.LightTheme, LIGHT)
        DARK -> setTheme(R.style.DarkTheme, DARK)
        SYSTEM_DEFAULT -> {
            when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_YES -> setTheme(R.style.DarkTheme, DARK)
                Configuration.UI_MODE_NIGHT_NO -> setTheme(R.style.LightTheme, LIGHT)
            }
        }
        else -> setTheme(R.style.LightTheme, LIGHT)
    }
}

fun Activity.setTheme(resId: Int, themeValue: String) {
    setTheme(resId)
    currentTheme = themeValue
}