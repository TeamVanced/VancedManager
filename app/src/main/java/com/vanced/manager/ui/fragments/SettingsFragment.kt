package com.vanced.manager.ui.fragments

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.widget.Toast
import androidx.preference.*
import com.google.firebase.messaging.FirebaseMessaging
import com.vanced.manager.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        activity?.title = getString(R.string.title_settings)
        setHasOptionsMenu(true)

            findPreference<Preference>("update_check")?.setOnPreferenceClickListener {
            UpdateCheckFragment().show(childFragmentManager.beginTransaction(), "Update Center")
            true
        }

        findPreference<SwitchPreference>("vanced_notifs")?.apply {
            title = getString(R.string.push_notifications, "Vanced")
            summary = getString(R.string.push_notifications_summary, "Vanced")
            setOnPreferenceChangeListener { _, newValue ->
                when (newValue) {
                    true -> FirebaseMessaging.getInstance().subscribeToTopic("Vanced-Update")
                    false -> FirebaseMessaging.getInstance().unsubscribeFromTopic("Vanced-Update")
                }
                true
            }
        }

        findPreference<SwitchPreference>("microg_notifs")?.apply {
            title = getString(R.string.push_notifications, "microG")
            summary = getString(R.string.push_notifications_summary, "microG")
            setOnPreferenceChangeListener { _, newValue ->
                when (newValue) {
                    true -> FirebaseMessaging.getInstance().subscribeToTopic("MicroG-Update")
                    false -> FirebaseMessaging.getInstance().unsubscribeFromTopic("MicroG-Update")
                }
                Toast.makeText(requireActivity(), "Set value to $newValue", Toast.LENGTH_SHORT).show()
                true
            }
        }

        val themePref = preferenceScreen.sharedPreferences.getString("theme_mode", "Follow System")
        findPreference<ListPreference>("theme_mode")?.apply {
            summary = when (themePref) {
                    "Light" -> getString(R.string.theme_light)
                    "Dark" -> getString(R.string.theme_dark)
                    else -> getString(R.string.theme_follow)
                }
            setOnPreferenceChangeListener { _, newValue ->
                if (themePref != newValue) {
                    requireActivity().recreate()
                    return@setOnPreferenceChangeListener true
                }
                false
            }
        }

        val accentPref = preferenceScreen.sharedPreferences.getString("accent_color", "Blue")
        findPreference<ListPreference>("accent_color")?.apply {
            summary = when (accentPref) {
                    "Blue" -> getString(R.string.accent_blue)
                    "Red" -> getString(R.string.accent_red)
                    "Green" -> getString(R.string.accent_green)
                    "Yellow" -> getString(R.string.accent_yellow)
                    else -> getString(R.string.accent_purple)
                }
            setOnPreferenceChangeListener { _, newValue ->
                if (accentPref != newValue) {
                    requireActivity().recreate()
                    return@setOnPreferenceChangeListener true
                }
                false
            }
        }

        val chosenPrefs: Preference? = findPreference("vanced_chosen_modes")
        chosenPrefs?.setOnPreferenceClickListener {
            ChosenPreferenceDialogFragment().show(childFragmentManager.beginTransaction(), "Chosen Preferences")
            true
        }

        val installUrl: Preference? = findPreference("install_url")
        installUrl?.setOnPreferenceClickListener {
            URLChangeFragment().show(childFragmentManager.beginTransaction(), "Install URL")
            true
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val devSettings = PreferenceManager.getDefaultSharedPreferences(activity).getBoolean("devSettings", false)
        if (devSettings) {
            inflater.inflate(R.menu.dev_settings_menu, menu)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

}