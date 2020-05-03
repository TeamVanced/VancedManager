package com.vanced.manager.ui.fragments

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.preference.PreferenceFragmentCompat
import com.vanced.manager.R

class SecretSettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.secret_settings, rootKey)
        activity?.title = "Secret Settings"
        setHasOptionsMenu(true)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dev_settings_menu, menu)
        super .onCreateOptionsMenu(menu, inflater)
    }

}
