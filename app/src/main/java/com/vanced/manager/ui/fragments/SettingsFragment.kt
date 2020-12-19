package com.vanced.manager.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.perf.FirebasePerformance
import com.vanced.manager.R
import com.vanced.manager.adapter.GetNotifAdapter
import com.vanced.manager.core.ui.base.BindingFragment
import com.vanced.manager.core.ui.ext.showDialog
import com.vanced.manager.databinding.FragmentSettingsBinding
import com.vanced.manager.ui.dialogs.*
import com.vanced.manager.utils.Extensions.toHex
import com.vanced.manager.utils.LanguageHelper.getLanguageFormat
import com.vanced.manager.utils.ThemeHelper.accentColor
import com.vanced.manager.utils.ThemeHelper.defAccentColor
import java.io.File

class SettingsFragment : BindingFragment<FragmentSettingsBinding>() {

    private companion object {

        const val LIGHT = "Light"
        const val DARK = "Dark"
    }

    private val prefs by lazy { getDefaultSharedPreferences(requireActivity()) }

    override fun binding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentSettingsBinding.inflate(inflater, container, false)

    override fun otherSetups() {
        setHasOptionsMenu(true)
        bindData()
    }

    private fun bindData() {
        with(binding) {
            bindRecycler()
            bindFirebase()
            bindManagerVariant()
            bindClearFiles()
            bindManagerTheme()
            bindManagerAccentColor()
            bindManagerLanguage()
            selectApps.setOnClickListener { showDialog(SelectAppsDialog()) }
        }
    }

    private fun FragmentSettingsBinding.bindRecycler() {
        notificationsRecycler.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = GetNotifAdapter(requireActivity())
        }
    }

    private fun FragmentSettingsBinding.bindFirebase() {
        firebase.setOnCheckedListener { _, isChecked ->
            FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(isChecked)
            FirebasePerformance.getInstance().isPerformanceCollectionEnabled = isChecked
            FirebaseAnalytics.getInstance(requireActivity()).setAnalyticsCollectionEnabled(isChecked)
        }
    }

    private fun FragmentSettingsBinding.bindManagerVariant() {
        managerVariant.apply {
            prefs.getString("vanced_variant", "nonroot")?.let { setSummary(it) }
            setOnClickListener { showDialog(ManagerVariantDialog()) }
        }
    }
    private fun FragmentSettingsBinding.bindClearFiles() {
        clearFiles.setOnClickListener {
            with(requireActivity()) {
                listOf("vanced/nonroot", "vanced/root", "music/nonroot", "music/root", "microg").forEach { dir ->
                    File(getExternalFilesDir(dir)?.path.toString()).deleteRecursively()
                }
                Toast.makeText(this, getString(R.string.cleared_files), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun FragmentSettingsBinding.bindManagerTheme() {
        val themePref = prefs.getString("manager_theme", "System Default")
        managerTheme.apply {
            setSummary(
                when (themePref) {
                    LIGHT -> getString(R.string.theme_light)
                    DARK -> getString(R.string.theme_dark)
                    else -> getString(R.string.system_default)
                }
            )
            setOnClickListener { showDialog(ManagerThemeDialog()) }
        }
    }

    private fun FragmentSettingsBinding.bindManagerAccentColor() {
        managerAccentColor.setSummary(prefs.getInt("manager_accent_color", defAccentColor).toHex())
        managerAccentColor.apply {
            setOnClickListener { showDialog(ManagerAccentColorDialog()) }
            accentColor.observe(viewLifecycleOwner) {
                managerAccentColor.setSummary(prefs.getInt("manager_accent_color", defAccentColor).toHex())
            }
        }
    }

    private fun FragmentSettingsBinding.bindManagerLanguage() {
        val langPref = prefs.getString("manager_lang", "System Default")
        managerLanguage.apply {
            setSummary(getLanguageFormat(requireActivity(), requireNotNull(langPref)))
            setOnClickListener { showDialog(ManagerLanguageDialog()) }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val devSettings = getDefaultSharedPreferences(requireActivity()).getBoolean("devSettings", false)
        if (devSettings) {
            inflater.inflate(R.menu.dev_settings_menu, menu)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }
}