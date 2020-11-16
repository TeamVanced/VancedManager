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
import com.vanced.manager.core.ext.showDialogRefl
import com.vanced.manager.databinding.FragmentSettingsBinding
import com.vanced.manager.ui.core.BindingFragment
import com.vanced.manager.ui.dialogs.*
import com.vanced.manager.utils.LanguageHelper.getLanguageFormat
import java.io.File

class SettingsFragment : BindingFragment<FragmentSettingsBinding>() {

    private companion object {

        const val LIGHT = "Light"
        const val DARK = "Dark"
        const val BLUE = "Blue"
        const val RED = "Red"
        const val GREEN = "Green"
        const val YELLOW = "Yellow"
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
            selectApps.setOnClickListener { showDialogRefl<SelectAppsDialog>() }
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
            setOnClickListener { showDialogRefl<ManagerVariantDialog>() }
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
                    DARK  -> getString(R.string.theme_dark)
                    else  -> getString(R.string.system_default)
                }
            )
            setOnClickListener { showDialogRefl<ManagerThemeDialog>() }
        }
    }

    private fun FragmentSettingsBinding.bindManagerAccentColor() {
        val accentPref = prefs.getString("manager_accent", "Blue")
        managerAccentColor.apply {
            setSummary(
                when (accentPref) {
                    BLUE   -> getString(R.string.accent_blue)
                    RED    -> getString(R.string.accent_red)
                    GREEN  -> getString(R.string.accent_green)
                    YELLOW -> getString(R.string.accent_yellow)
                    else   -> getString(R.string.accent_purple)
                }
            )
            setOnClickListener { showDialogRefl<ManagerAccentColorDialog>() }
        }
    }

    private fun FragmentSettingsBinding.bindManagerLanguage() {
        val langPref = prefs.getString("manager_lang", "System Default")
        managerLanguage.apply {
            setSummary(getLanguageFormat(requireActivity(), requireNotNull(langPref)))
            setOnClickListener { showDialogRefl<ManagerLanguageDialog>() }
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