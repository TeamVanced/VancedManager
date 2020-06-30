package com.vanced.manager.ui.fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.vanced.manager.R
import com.vanced.manager.ui.MainActivity

class DevSettingsFragment: PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.dev_settings, rootKey)

        val ftSwitch: Preference? = findPreference("firststart_switch")

        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        ftSwitch?.setOnPreferenceClickListener {

            AlertDialog.Builder(requireContext())
                .setTitle("FirstStart activated")
                .setMessage("boolean will be activated on next app start")
                .setPositiveButton("Restart") { _, _ ->
                    run {
                        startActivity(Intent(requireContext(), MainActivity::class.java))
                        activity?.finish()
                    }
                }
                .create()
                .show()

            prefs.edit().putBoolean("firstStart", true).apply()
            true

        }
        val archPref: Preference? = findPreference("device_arch")
        val supportedAbis: Array<String> = Build.SUPPORTED_ABIS

        if (supportedAbis.contains("arm64-v8a") || supportedAbis.contains("x86_64")) {
            archPref?.summary = "64bit"
        } else {
            archPref?.summary = "32bit"
        }

    }
}