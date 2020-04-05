package com.vanced.manager

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import com.vanced.manager.ui.MainActivity

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        (activity as MainActivity).supportActionBar?.title = getString(R.string.settings)

       val themeSwitch: ListPreference? = findPreference("theme_modes")
        themeSwitch?.setOnPreferenceChangeListener { _, _ ->
            val currentVal: String = themeSwitch.value
            when (currentVal.toInt()){
                 0 -> {
                     AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                 }
                 1 -> {
                     AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                 }
                else ->{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
            }
            true
    }


    }
}