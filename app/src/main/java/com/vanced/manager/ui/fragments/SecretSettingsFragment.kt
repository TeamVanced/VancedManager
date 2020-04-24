package com.vanced.manager.ui.fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.vanced.manager.R

class SecretSettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.secret_settings, rootKey)
        activity?.title = "Secret Settings"

    }
}
