package com.vanced.manager.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.vanced.manager.R
import com.vanced.manager.ui.MainActivity

class DevSettingsFragment: PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.dev_settings, rootKey)

        val ftSwitch: SwitchPreference? = findPreference("firststart_switch")

        val prefs = activity?.getSharedPreferences("prefs", AppCompatActivity.MODE_PRIVATE)
        val editor = prefs?.edit()
        ftSwitch?.setOnPreferenceChangeListener { _, _ ->

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

            editor?.putBoolean("firstStart", true)
            editor?.apply()
            true

        }
    }
}