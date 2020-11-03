package com.vanced.manager.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreferenceCompat
import com.crowdin.platform.Crowdin
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

        findPreference<SwitchPreferenceCompat>("crowdin_upload_screenshot")?.isVisible = Crowdin.isAuthorized()
        findPreference<SwitchPreferenceCompat>("crowdin_real_time")?.isVisible = Crowdin.isAuthorized()

        findPreference<Preference>("crowdin_auth")?.setOnPreferenceClickListener {

            @RequiresApi(Build.VERSION_CODES.M)
            if (!Settings.canDrawOverlays(requireActivity())) {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + requireActivity().packageName)
                )
                startActivityForResult(intent, 69)
                return@setOnPreferenceClickListener true
            }

            Crowdin.authorize(requireActivity())
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
            ManagerUpdateDialog(true).show(
                requireActivity().supportFragmentManager,
                "update_manager"
            )
            true
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 69) {
            @RequiresApi(23)
            if (Settings.canDrawOverlays(requireActivity())) {
                Crowdin.authorize(requireActivity())
            }
        }
    }

}