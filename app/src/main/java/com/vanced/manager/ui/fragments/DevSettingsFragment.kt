package com.vanced.manager.ui.fragments

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.vanced.manager.R

class DevSettingsFragment: PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.dev_settings, rootKey)

        val ftSwitch: SwitchPreference? = findPreference("firststart_switch")
        //val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(requireContext())

        //ftSwitch?.isChecked = sharedPrefs.getBoolean("firststart_switch", false)
        val prefs = activity?.getSharedPreferences("prefs", AppCompatActivity.MODE_PRIVATE)
        val editor = prefs?.edit()
        ftSwitch?.setOnPreferenceChangeListener { _, isChecked ->
            when (isChecked) {
                true -> {
                    editor?.putBoolean("firstStart", true)
                    editor?.apply()
                    true
                }
                else -> {
                    editor?.putBoolean("firstStart", false)
                    editor?.apply()
                    true
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dev_settings_menu, menu)
        super .onCreateOptionsMenu(menu, inflater)
    }
}