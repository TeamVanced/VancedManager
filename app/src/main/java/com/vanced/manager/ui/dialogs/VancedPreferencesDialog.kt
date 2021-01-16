package com.vanced.manager.ui.dialogs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.edit
import com.vanced.manager.R
import com.vanced.manager.core.ui.base.BindingBottomSheetDialogFragment
import com.vanced.manager.core.ui.ext.showDialog
import com.vanced.manager.databinding.DialogVancedPreferencesBinding
import com.vanced.manager.utils.*
import java.util.*

class VancedPreferencesDialog : BindingBottomSheetDialogFragment<DialogVancedPreferencesBinding>() {

    companion object {

        fun newInstance(): VancedPreferencesDialog = VancedPreferencesDialog().apply {
            arguments = Bundle()
        }
    }

    private val defPrefs by lazy { requireActivity().getDefaultPrefs() }
    private val installPrefs by lazy { requireActivity().getSharedPreferences("installPrefs", Context.MODE_PRIVATE) }

    override fun binding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DialogVancedPreferencesBinding.inflate(inflater, container, false)

    override fun otherSetups() {
        bindData()
    }

    private fun bindData() {
        with(binding) {
            val showLang = mutableListOf<String>()
            installPrefs.getString("lang", getDefaultVancedLanguages())?.split(", ")?.toTypedArray()?.forEach { lang ->
                val loc = Locale(lang)
                showLang.add(loc.getDisplayLanguage(loc).capitalize(Locale.ROOT))
            }
            val vancedVersionsConv = vancedVersions.value?.value?.reversed()?.convertToAppVersions()
            vancedInstallTitle.text = getString(R.string.app_installation_preferences, getString(R.string.vanced))
            vancedTheme.text = getString(R.string.chosen_theme, installPrefs.getString("theme", "dark")?.convertToAppTheme(requireActivity()))
            vancedVersion.text = getString(R.string.chosen_version, defPrefs.getString("vanced_version", "latest"))
            vancedLang.text = getString(R.string.chosen_lang, showLang)
            openThemeSelector.setOnClickListener {
                dismiss()
                showDialog(VancedThemeSelectorDialog())
            }
            openVersionSelector.setOnClickListener {
                dismiss()
                showDialog(
                    AppVersionSelectorDialog.newInstance(
                        versions = vancedVersionsConv,
                        app = "vanced"
                    )
                )
            }
            openLanguageSelector.setOnClickListener {
                dismiss()
                showDialog(VancedLanguageSelectionDialog())
            }
            vancedInstall.setOnClickListener {
                if (showLang.isEmpty()) {
                    installPrefs.edit {
                        putString("lang", "en")
                    }
                }
                dismiss()
                showDialog(
                    AppDownloadDialog.newInstance(
                        app = getString(R.string.vanced)
                    )
                )
            }
        }
    }
}
