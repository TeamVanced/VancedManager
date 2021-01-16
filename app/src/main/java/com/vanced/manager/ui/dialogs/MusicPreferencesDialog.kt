package com.vanced.manager.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.vanced.manager.R
import com.vanced.manager.core.ui.base.BindingBottomSheetDialogFragment
import com.vanced.manager.core.ui.ext.showDialog
import com.vanced.manager.databinding.DialogMusicPreferencesBinding
import com.vanced.manager.utils.convertToAppVersions
import com.vanced.manager.utils.getDefaultPrefs
import com.vanced.manager.utils.musicVersions

class MusicPreferencesDialog : BindingBottomSheetDialogFragment<DialogMusicPreferencesBinding>() {

    companion object {

        fun newInstance(): MusicPreferencesDialog = MusicPreferencesDialog().apply {
            arguments = Bundle()
        }
    }

    private val prefs by lazy { requireActivity().getDefaultPrefs() }

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
            val musicVersionsConv = musicVersions.value?.value?.reversed()?.convertToAppVersions()
            musicInstallTitle.text = getString(R.string.app_installation_preferences, getString(R.string.music))
            musicVersion.text = getString(R.string.chosen_version, prefs.getString("music_version", "latest"))
            openVersionSelector.setOnClickListener {
                dismiss()
                showDialog(
                    AppVersionSelectorDialog.newInstance(
                        versions = musicVersionsConv,
                        app = "music"
                    )
                )
            }
            musicInstall.setOnClickListener {
                dismiss()
                showDialog(
                    AppDownloadDialog.newInstance(
                        app = getString(R.string.music)
                    )
                )
            }
        }
    }
}