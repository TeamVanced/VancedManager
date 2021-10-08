package com.vanced.manager.ui.fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.vanced.manager.core.ui.base.BindingFragment
import com.vanced.manager.databinding.FragmentDevSettingsBinding
import com.vanced.manager.ui.WelcomeActivity
import com.vanced.manager.ui.dialogs.ManagerUpdateDialog
import com.vanced.manager.ui.dialogs.URLChangeDialog

class DevSettingsFragment : BindingFragment<FragmentDevSettingsBinding>() {

    private val prefs by lazy { getDefaultSharedPreferences(requireActivity()) }

    override fun binding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentDevSettingsBinding.inflate(inflater, container, false)

    override fun otherSetups() {
        setHasOptionsMenu(true)
        bindData()
    }

    private fun bindData() {
        with(binding) {
            bindWelcomeLauncher()
            bindForceUpdate()
            bindChannelURL()
            bindKernelArch()
            bindAndroidVersion()
        }
    }

    private fun FragmentDevSettingsBinding.bindWelcomeLauncher() {
        welcomeScreenLauncher.setOnClickListener {
            prefs.edit {
                putBoolean("firstLaunch", true)
                putBoolean("show_changelog_tooltip", true)
            }
            startActivity(Intent(requireContext(), WelcomeActivity::class.java))
            requireActivity().finish()
        }
    }

    private fun FragmentDevSettingsBinding.bindForceUpdate() {
        forceManagerUpdate.setOnClickListener {
            ManagerUpdateDialog.newInstance(true).show(
                requireActivity().supportFragmentManager,
                "update_manager"
            )
        }
    }

    private fun FragmentDevSettingsBinding.bindChannelURL() {
        channelUrl.setOnClickListener {
            URLChangeDialog().show(childFragmentManager.beginTransaction(), null)
        }
    }

    private fun FragmentDevSettingsBinding.bindKernelArch() {
        val supportedAbis: Array<String> = Build.SUPPORTED_ABIS

        kernelArch.setSummary(
            if (supportedAbis.contains("arm64-v8a") || supportedAbis.contains("x86_64")) {
                "64bit"
            } else {
                "32bit"
            }
        )
    }

    private fun FragmentDevSettingsBinding.bindAndroidVersion() {
        androidVersion.setSummary("${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})")
    }
}