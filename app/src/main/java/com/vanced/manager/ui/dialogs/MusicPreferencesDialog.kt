package com.vanced.manager.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.vanced.manager.R
import com.vanced.manager.core.ui.base.BindingBottomSheetDialogFragment
import com.vanced.manager.core.ui.ext.showDialog
import com.vanced.manager.databinding.DialogMusicPreferencesBinding
import com.vanced.manager.utils.*

class MusicPreferencesDialog : BindingBottomSheetDialogFragment<DialogMusicPreferencesBinding>() {

    companion object {

        fun newInstance(): MusicPreferencesDialog = MusicPreferencesDialog().apply {
            arguments = Bundle()
        }
    }

    private val prefs by lazy { requireActivity().defPrefs }

    override fun binding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DialogMusicPreferencesBinding.inflate(inflater, container, false)

    override fun otherSetups() {
        bindData()
    }

    private fun bindData() {
        with(binding) {
            val musicVersionsConv = musicVersions.value?.value?.convertToAppVersions()
            musicInstallTitle.text = getString(R.string.app_installation_preferences, getString(R.string.music))
            musicVersion.text = getString(R.string.chosen_version, prefs.musicVersion?.formatVersion(requireActivity()))
            openVersionSelectorLayout.setOnClickListener {
                dismiss()
                showDialog(
                    AppVersionSelectorDialog.newInstance(
                        versions = musicVersionsConv,
                        app = "music"
                    )
                )
            }
            musicInstall.setOnClickListener {
                fun downloadMusic(version: String? = null) {
                    dismiss()
                    showDialog(
                        AppDownloadDialog.newInstance(
                            app = getString(R.string.music),
                            version = version
                        )
                    )
                }
                if (prefs.managerVariant == "nonroot" && isMicrogBroken && prefs.musicVersion?.getLatestAppVersion(musicVersions.value?.value ?: listOf(""))?.replace(".", "")?.take(3)?.toIntOrNull() ?: 0 >= 411 &&
                    !PackageHelper.isPackageInstalled(
                        AppUtils.musicPkg,
                        requireActivity().packageManager
                    )
                ) {
                    MaterialAlertDialogBuilder(requireActivity()).apply {
                        setTitle(R.string.microg_bug)
                        setMessage(R.string.microg_bug_summary_music)
                        setPositiveButton(R.string.auth_dialog_ok) { _, _ ->
                            downloadMusic("4.07.51")
                        }
                        setNeutralButton(R.string.cancel) { _, _ ->
                            dismiss()
                        }
                        create()
                    }.showWithAccent()
                    return@setOnClickListener
                }

                downloadMusic()
            }
        }
    }
}