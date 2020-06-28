package com.vanced.manager.ui.fragments

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
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
        themeSwitch?.summary =
            preferenceScreen.sharedPreferences.getString("theme_mode", "Follow System")
        themeSwitch?.setOnPreferenceChangeListener { _, _ ->
            activity?.recreate()
            true
        }

        val accentSwitch: ListPreference? = findPreference("accent_color")
        accentSwitch?.summary = preferenceScreen.sharedPreferences.getString("accent_color", "Blue")
        accentSwitch?.setOnPreferenceChangeListener { _, _ ->
            activity?.recreate()
            true
        }

        val chosenPrefs: Preference? = findPreference("vanced_chosen_modes")
        chosenPrefs?.setOnPreferenceClickListener {
            val fm = childFragmentManager.beginTransaction()
            val chosenPrefsDialog = ChosenPreferenceDialogFragment()
            chosenPrefsDialog.show(fm, "Chosen Preferences")
            true
        }

        val installUrl: Preference? = findPreference("install_url")
        installUrl?.setOnPreferenceClickListener {
            val fm = childFragmentManager.beginTransaction()
            val chosenPrefsDialog = URLChangeFragment()
            chosenPrefsDialog.show(fm, "Install URL")
            true
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val devSettings = PreferenceManager.getDefaultSharedPreferences(activity).getBoolean("devSettings", false)
        if (devSettings) {
            inflater.inflate(R.menu.dev_settings_menu, menu)
        }
        super .onCreateOptionsMenu(menu, inflater)
    }

}