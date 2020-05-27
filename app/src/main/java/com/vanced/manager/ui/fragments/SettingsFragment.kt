package com.vanced.manager.ui.fragments

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.preference.*
import com.vanced.manager.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        activity?.title = getString(R.string.title_settings)
        setHasOptionsMenu(true)

        val updateCheck: Preference? = findPreference("update_check")
        updateCheck?.setOnPreferenceClickListener {
            val fm = childFragmentManager.beginTransaction()
            val updateDialog = UpdateCheckFragment()
            updateDialog.show(fm, "Update Center")
            true
        }
        
       val themeSwitch: ListPreference? = findPreference("theme_mode")
        themeSwitch?.setOnPreferenceChangeListener { _, _ ->
            activity?.recreate()
            true
        }

        val accentSwitch: ListPreference? = findPreference("accent_color")
        accentSwitch?.setOnPreferenceChangeListener { _, _ ->
            activity?.recreate()
            true
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.secret_settings_menu, menu)
        super .onCreateOptionsMenu(menu, inflater)
    }

}