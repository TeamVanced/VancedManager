package com.vanced.manager.ui.fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.vanced.manager.R
import com.vanced.manager.ui.WelcomeActivity
import com.vanced.manager.ui.dialogs.ManagerUpdateDialog
import com.vanced.manager.ui.dialogs.URLChangeDialog

class DevSettingsFragment: PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.dev_settings, rootKey)

        val ftSwitch: Preference? = findPreference("firstlaunch_switch")

        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        ftSwitch?.setOnPreferenceClickListener {

            AlertDialog.Builder(requireContext())
                .setTitle("FirstLaunch activated")
                .setMessage("boolean will be activated on next app start")
                .setPositiveButton("Restart") { _, _ ->
                    run {
                        startActivity(Intent(requireContext(), WelcomeActivity::class.java))
                        activity?.finish()
                    }
                }
                .create()
                .show()

            prefs.edit().putBoolean("firstLaunch", true).apply()
            prefs.edit().putBoolean("show_changelog_tooltip", true).apply()
            true

        }

        findPreference<Preference>("install_url")?.setOnPreferenceClickListener {
            URLChangeDialog().show(childFragmentManager.beginTransaction(), "Install URL")
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
            ManagerUpdateDialog(true).show(requireActivity().supportFragmentManager, "update_manager")
            true
        }

    }

}