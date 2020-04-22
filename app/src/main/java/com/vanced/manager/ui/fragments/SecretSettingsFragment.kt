package com.vanced.manager.ui.fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.vanced.manager.R

class SecretSettingsFragment : PreferenceFragmentCompat() {

    override fun onStart() {
        super.onStart()
        activity?.title = "Secret Settings"
        setHasOptionsMenu(true)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

    }
}
