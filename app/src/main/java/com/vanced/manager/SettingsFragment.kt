package com.vanced.manager

import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import com.vanced.manager.ui.MainActivity

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        (activity as MainActivity).supportActionBar?.title = getString(R.string.settings)

       val themeSwitch: ListPreference? = findPreference("theme_modes")
        themeSwitch?.setOnPreferenceChangeListener { _, _ ->

            when (themeSwitch.value){
                "LIGHT" -> {
                    activity?.setTheme(R.style.LightTheme_Blue)
                    activity?.recreate()
                }
                "DARK" -> {
                    activity?.setTheme(R.style.DarkTheme_Blue)
                    activity?.recreate()
                }
                 else -> {
                     activity?.setTheme(R.style.LightTheme_Blue)
                     activity?.recreate()
                 }
            }
            true
    }


    }
}