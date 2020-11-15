package com.vanced.manager.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vanced.manager.R
import com.vanced.manager.databinding.DialogMusicPreferencesBinding
import com.vanced.manager.utils.Extensions.convertToAppVersions
import com.vanced.manager.utils.Extensions.getDefaultPrefs
import com.vanced.manager.utils.Extensions.show
import com.vanced.manager.utils.InternetTools.musicVersions

class MusicPreferencesDialog : BottomSheetDialogFragment() {

    private lateinit var binding: DialogMusicPreferencesBinding
    private val prefs by lazy { requireActivity().getDefaultPrefs() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_music_preferences, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val musicVersionsConv = musicVersions.get()?.value?.reversed()?.convertToAppVersions()

        binding.musicInstallTitle.text = requireActivity().getString(R.string.app_installation_preferences, requireActivity().getString(R.string.music))
        binding.musicVersion.text = requireActivity().getString(R.string.chosen_version, prefs.getString("music_version", "latest"))

        binding.openVersionSelector.setOnClickListener {
            dismiss()
            AppVersionSelectorDialog(musicVersionsConv, "music").show(requireActivity())
        }
        binding.musicInstall.setOnClickListener {
            dismiss()
            AppDownloadDialog.newInstance(
                app = getString(R.string.music)
            ).show(requireActivity())
        }
    }
}