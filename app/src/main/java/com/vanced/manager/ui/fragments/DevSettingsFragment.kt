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
import com.vanced.manager.utils.DownloadHelper.downloadManager

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

        findPreference<Preference>("install_url")?.setOnPreferenceClickListener {
            URLChangeFragment().show(childFragmentManager.beginTransaction(), "Install URL")
            true
        }

        val supportedAbis: Array<String> = Build.SUPPORTED_ABIS

        findPreference<Preference>("device_arch")?.summary =
            if (supportedAbis.contains("arm64-v8a") || supportedAbis.contains("x86_64")) {
                "64bit"
            } else {
                "32bit"
            }

        findPreference<Preference>("device_os")?.summary = "${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})"

        val forceUpdate: Preference? = findPreference("force_update")
        forceUpdate?.setOnPreferenceClickListener {
            downloadManager(true, requireActivity())
            true
        }

    }

}