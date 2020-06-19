package com.vanced.manager.ui.core

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.preference.PreferenceManager
import com.vanced.manager.R

// This activity will NOT be used in manifest
// since MainActivity will extend it
@SuppressLint("Registered")
open class ThemedActivity : AppCompatActivity() {

    private lateinit var currentTheme: String
    private lateinit var pref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            setTaskBG()
        }
    }

    override fun onResume() {
        super.onResume()
        //set Task Header color in recents menu for
        //devices that are not using pie recents
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            setTaskBG()
        }
    }

    private fun setTaskBG() {
        val label = getString(R.string.app_name)
        val color = ResourcesCompat.getColor(resources, R.color.Black, null)
        val taskDec: ActivityManager.TaskDescription =
            ActivityManager.TaskDescription(label, null, color)
        setTaskDescription(taskDec)

    }

}