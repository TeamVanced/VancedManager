package com.vanced.manager.ui.dialogs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vanced.manager.R
import com.vanced.manager.adapter.VancedPrefArray
import com.vanced.manager.databinding.DialogInstallationPreferencesBinding
import com.vanced.manager.model.VancedPrefModel
import com.vanced.manager.utils.LanguageHelper.getDefaultVancedLanguages
import java.util.*

class VancedPreferencesDialog : BottomSheetDialogFragment() {

    private lateinit var binding: DialogInstallationPreferencesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_installation_preferences, container, false)
        return binding.root
    }

    @ExperimentalStdlibApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = requireActivity().getSharedPreferences("installPrefs", Context.MODE_PRIVATE)
        val langPrefs = prefs.getString("lang", getDefaultVancedLanguages(requireActivity()))?.split(", ")?.toTypedArray()
        val showLang = mutableListOf<String>()
        if (langPrefs != null) {
            for (lang in langPrefs) {
                val loc = Locale(lang)
                showLang.add(loc.getDisplayLanguage(loc).capitalize(Locale.ROOT))
            }
        }

        val darkTheme = VancedPrefModel(
                requireActivity().getString(R.string.install_light_dark),
                "dark"
        )

        val blackTheme = VancedPrefModel(
                requireActivity().getString(R.string.install_light_black),
                "black"
        )

        val adapter = arrayOf(darkTheme, blackTheme)
        binding.themeSpinner.adapter = VancedPrefArray(requireActivity(), android.R.layout.simple_spinner_dropdown_item, adapter)
        binding.themeSpinner.setSelection(if (prefs.getString("theme", "dark") == "dark") 0 else 1)

        binding.openLanguageSelector.setOnClickListener {
            dismiss()
            VancedLanguageSelectionDialog().show(requireActivity().supportFragmentManager, "")
        }

        binding.chosenLang.text = requireActivity().getString(R.string.chosen_lang, showLang)

        binding.themeSpinner.onItemSelectedListener = object : OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                prefs.edit().putString("theme", adapter[position].value).apply()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                return
            }
        }

        binding.chosenPrefsInstall.setOnClickListener {
            dismiss()
            AppDownloadDialog(requireActivity().getString(R.string.vanced)).show(requireActivity().supportFragmentManager, "InstallVanced")
        }
    }
}
