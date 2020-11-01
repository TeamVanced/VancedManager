package com.vanced.manager.ui.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.perf.FirebasePerformance
import com.vanced.manager.R
import com.vanced.manager.adapter.GetNotifAdapter
import com.vanced.manager.databinding.FragmentSettingsBinding
import com.vanced.manager.ui.dialogs.*
import com.vanced.manager.utils.LanguageHelper.getLanguageFormat
import java.io.File

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val prefs by lazy { getDefaultSharedPreferences(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.notificationsRecycler.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = GetNotifAdapter(requireActivity())
        }

        binding.firebase.setOnCheckedListener { _, isChecked ->
            FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(isChecked)
            FirebasePerformance.getInstance().isPerformanceCollectionEnabled = isChecked
            FirebaseAnalytics.getInstance(requireActivity()).setAnalyticsCollectionEnabled(isChecked)
        }

        binding.managerVariant.apply {
            prefs.getString("vanced_variant", "nonroot")?.let { setSummary(it) }
            setOnClickListener {
                ManagerVariantDialog().show(requireActivity().supportFragmentManager, "")
            }
        }

        binding.clearFiles.setOnClickListener {
            with(requireActivity()) {
                listOf("vanced/nonroot", "vanced/root", "music/nonroot", "music/root", "microg").forEach { dir ->
                    File(getExternalFilesDir(dir)?.path.toString()).deleteRecursively()
                }
                Toast.makeText(this, getString(R.string.cleared_files), Toast.LENGTH_SHORT).show()
            }
        }

        val themePref = prefs.getString("manager_theme", "System Default")
        binding.managerTheme.apply {
            setSummary(
                    when (themePref) {
                        "Light" -> getString(R.string.theme_light)
                        "Dark" -> getString(R.string.theme_dark)
                        else -> getString(R.string.system_default)
                    }
            )
            setOnClickListener {
                ManagerThemeDialog().show(requireActivity().supportFragmentManager, "")
            }
        }

        val accentPref = prefs.getString("manager_accent", "Blue")
        binding.managerAccentColor.apply {
            setSummary(
                    when (accentPref) {
                        "Blue" -> getString(R.string.accent_blue)
                        "Red" -> getString(R.string.accent_red)
                        "Green" -> getString(R.string.accent_green)
                        "Yellow" -> getString(R.string.accent_yellow)
                        else -> getString(R.string.accent_purple)
                    }
            )
            setOnClickListener {
                ManagerAccentColorDialog().show(requireActivity().supportFragmentManager, "")
            }
        }

        val langPref = prefs.getString("manager_lang", "System Default")
        binding.managerLanguage.apply {
            setSummary(getLanguageFormat(requireActivity(), langPref!!))
            setOnClickListener {
                ManagerLanguageDialog().show(requireActivity().supportFragmentManager, "")
            }
        }

        binding.selectApps.setOnClickListener {
            SelectAppsDialog().show(requireActivity().supportFragmentManager, "")
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