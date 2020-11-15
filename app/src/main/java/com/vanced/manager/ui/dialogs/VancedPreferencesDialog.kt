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
import com.vanced.manager.utils.Extensions.getDefaultPrefs
import com.vanced.manager.utils.Extensions.show
import com.vanced.manager.utils.InternetTools.vancedVersions
import com.vanced.manager.utils.LanguageHelper.getDefaultVancedLanguages
import java.util.*

class VancedPreferencesDialog : BottomSheetDialogFragment() {

    private lateinit var binding: DialogVancedPreferencesBinding
    private val defPrefs by lazy { requireActivity().getDefaultPrefs() }
    private val installPrefs by lazy { requireActivity().getSharedPreferences("installPrefs", Context.MODE_PRIVATE) }

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

        val showLang = mutableListOf<String>()
        installPrefs.getString("lang", getDefaultVancedLanguages())?.split(", ")?.toTypedArray()?.forEach { lang ->
            val loc = Locale(lang)
            showLang.add(loc.getDisplayLanguage(loc).capitalize(Locale.ROOT))
        }

        val vancedVersionsConv = vancedVersions.get()?.value?.reversed()?.convertToAppVersions()

        binding.vancedInstallTitle.text = requireActivity().getString(R.string.app_installation_preferences, requireActivity().getString(R.string.vanced))

        binding.vancedTheme.text = requireActivity().getString(R.string.chosen_theme, installPrefs.getString("theme", "dark")?.convertToAppTheme(requireActivity()))
        binding.vancedVersion.text = requireActivity().getString(R.string.chosen_version, defPrefs.getString("vanced_version", "latest"))
        binding.vancedLang.text = requireActivity().getString(R.string.chosen_lang, showLang)

        binding.openThemeSelector.setOnClickListener {
            dismiss()
            VancedThemeSelectorDialog().show(requireActivity())
        }

        binding.openVersionSelector.setOnClickListener {
            dismiss()
            AppVersionSelectorDialog(vancedVersionsConv, "vanced").show(requireActivity())
        }

        binding.openLanguageSelector.setOnClickListener {
            dismiss()
            VancedLanguageSelectionDialog().show(requireActivity())
        }

        binding.vancedInstall.setOnClickListener {
            dismiss()
            AppDownloadDialog.newInstance(
                app = getString(R.string.vanced)
            ).show(requireActivity())
        }
    }
}
