package com.vanced.manager.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.vanced.manager.R
import com.vanced.manager.core.ui.base.BindingBottomSheetDialogFragment
import com.vanced.manager.core.ui.ext.showDialog
import com.vanced.manager.databinding.DialogVancedPreferencesBinding
import com.vanced.manager.utils.*
import com.vanced.manager.utils.AppUtils.vancedPkg
import com.vanced.manager.utils.PackageHelper.isPackageInstalled
import java.util.*

class VancedPreferencesDialog : BindingBottomSheetDialogFragment<DialogVancedPreferencesBinding>() {

    companion object {

        fun newInstance(): VancedPreferencesDialog = VancedPreferencesDialog().apply {
            arguments = Bundle()
        }
    }

    private val defPrefs by lazy { requireActivity().defPrefs }
    private val installPrefs by lazy { requireActivity().installPrefs }

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
            installPrefs.lang?.split(", ")?.toTypedArray()?.forEach { lang ->
                val loc = Locale(lang)
                showLang.add(loc.getDisplayLanguage(loc).capitalize(Locale.ROOT))
            }
            val vancedVersionsConv = vancedVersions.value?.value?.convertToAppVersions()
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
                    installPrefs.lang = "en"
                }

                fun downloadVanced(version: String? = null) {
                    dismiss()
                    showDialog(
                        AppDownloadDialog.newInstance(
                            app = getString(R.string.vanced),
                            version = version
                        )
                    )
                }

                if (defPrefs.managerVariant == "nonroot" && isMicrogBroken && installPrefs.vancedVersion?.getLatestAppVersion(vancedVersions.value?.value ?: listOf(""))?.take(2)?.toIntOrNull() == 16 && !isPackageInstalled(vancedPkg, requireActivity().packageManager)) {
                    MaterialAlertDialogBuilder(requireActivity()).apply {
                        setTitle(R.string.microg_bug)
                        setMessage(R.string.microg_bug_summary)
                        setPositiveButton(R.string.auth_dialog_ok) { _, _ ->
                            downloadVanced("15.43.32")
                        }
                        setNeutralButton(R.string.cancel) { _, _ ->
                            dismiss()
                        }
                        create()
                    }.applyAccent()
                    return@setOnClickListener
                }

                downloadVanced()
            }
        }
    }
}
