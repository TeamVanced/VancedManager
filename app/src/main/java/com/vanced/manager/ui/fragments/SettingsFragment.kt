package com.vanced.manager.ui.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.navigation.findNavController
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.vanced.manager.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onStart() {
        super.onStart()
        activity?.title = getString(R.string.title_settings)
        setHasOptionsMenu(true)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        val updateCheck: Preference? = findPreference("update_check")
        
       val themeSwitch: ListPreference? = findPreference("theme_mode")
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
                "FOLLOW" -> {
                    when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                        Configuration.UI_MODE_NIGHT_YES ->{
                            activity?.setTheme(R.style.DarkTheme_Blue)
                            activity?.recreate()
                        }
                        Configuration.UI_MODE_NIGHT_NO -> {
                            activity?.setTheme(R.style.LightTheme_Blue)
                            activity?.recreate()
                        }
                    }
                }
                else -> {
                    activity?.setTheme(R.style.LightTheme_Blue)
                    activity?.recreate()
                }
            }
            true
        }

    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.secret_settings_menu, menu)
        super .onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.secret_settings -> view?.findNavController()?.navigate(R.id.toSecretSettingsFragment)
        else -> null
    }?.let { true } ?: super.onOptionsItemSelected(item)

}