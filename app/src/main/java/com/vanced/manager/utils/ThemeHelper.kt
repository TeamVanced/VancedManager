package com.vanced.manager.utils

import android.app.Activity
import android.content.res.Configuration
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vanced.manager.R

const val defAccentColor: Int = -13732865

val mutableAccentColor = MutableLiveData<Int>()
val accentColor: LiveData<Int> = mutableAccentColor

fun Activity.setFinalTheme() {
    when (defPrefs.managerTheme) {
        "Light" -> setTheme(R.style.LightTheme)
        "Dark" -> setTheme(R.style.DarkTheme)
        "System Default" -> {
            when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_YES -> setTheme(R.style.DarkTheme)
                Configuration.UI_MODE_NIGHT_NO -> setTheme(R.style.LightTheme)
            }
        }
        else -> setTheme(R.style.LightTheme)
    }
}