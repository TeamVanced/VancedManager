package com.vanced.manager.ui.dialogs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vanced.manager.R
import com.vanced.manager.databinding.DialogVancedPreferencesBinding
import com.vanced.manager.utils.Extensions.convertToAppTheme
import com.vanced.manager.utils.Extensions.convertToAppVersions
import com.vanced.manager.utils.Extensions.show
import com.vanced.manager.utils.InternetTools.vancedVersions
import com.vanced.manager.utils.LanguageHelper.getDefaultVancedLanguages
import java.util.*

class VancedPreferencesDialog : BottomSheetDialogFragment() {

    private lateinit var binding: DialogVancedPreferencesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_vanced_preferences, container, false)
        return binding.root
    }

    @ExperimentalStdlibApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = requireActivity().getSharedPreferences("installPrefs", Context.MODE_PRIVATE)
        val langPrefs = prefs.getString("lang", getDefaultVancedLanguages())?.split(", ")?.toTypedArray()
        val showLang = mutableListOf<String>()
        if (langPrefs != null) {
            for (lang in langPrefs) {
                val loc = Locale(lang)
                showLang.add(loc.getDisplayLanguage(loc).capitalize(Locale.ROOT))
            }
        }

        val vancedVersionsConv = vancedVersions.get()?.value?.reversed()?.convertToAppVersions()

        binding.vancedInstallTitle.text = requireActivity().getString(R.string.app_installation_preferences, requireActivity().getString(R.string.vanced))

        binding.vancedTheme.text = requireActivity().getString(R.string.chosen_theme, prefs.getString("theme", "dark")?.convertToAppTheme(requireActivity()))
        binding.vancedVersion.text = requireActivity().getString(R.string.chosen_version, prefs.getString("vanced_version", vancedVersionsConv?.get(0)?.value ?: ""))
        binding.vancedLang.text = requireActivity().getString(R.string.chosen_lang, showLang)

        binding.openThemeSelector.setOnClickListener {
            dismiss()
            VancedThemeSelectorDialog().show(requireActivity())
        }

        binding.openVersionSelector.setOnClickListener {
            dismiss()
            if (vancedVersionsConv != null) {
                AppVersionSelectorDialog(vancedVersionsConv, "vanced").show(requireActivity())
            }
        }

        binding.openLanguageSelector.setOnClickListener {
            dismiss()
            VancedLanguageSelectionDialog().show(requireActivity())
        }

        binding.vancedInstall.setOnClickListener {
            dismiss()
            AppDownloadDialog(requireActivity().getString(R.string.vanced)).show(requireActivity().supportFragmentManager, "InstallVanced")
        }
    }
}
