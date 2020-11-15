package com.vanced.manager.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vanced.manager.R
import com.vanced.manager.core.downloader.MicrogDownloader.startMicrogInstall
import com.vanced.manager.core.downloader.MusicDownloader.startMusicInstall
import com.vanced.manager.core.downloader.VancedDownloader.startVancedInstall
import com.vanced.manager.databinding.DialogInstallationFilesDetectedBinding
import com.vanced.manager.utils.Extensions.show

class InstallationFilesDetectedDialog(private val app: String) : BottomSheetDialogFragment() {

    private lateinit var binding: DialogInstallationFilesDetectedBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_installation_files_detected, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.installationDetectedTitle.text = requireActivity().getString(R.string.app_install_files_detected, app)
        binding.installationDetectedSummary.text = requireActivity().getString(R.string.app_install_files_detected_summary, app)

        binding.installationDetectedRedownload.setOnClickListener {
            dismiss()
            if (app == requireActivity().getString(R.string.vanced))
                VancedPreferencesDialog().show(requireActivity())
            else {
                AppDownloadDialog.newInstance(app).show(requireActivity())
            }
        }

        binding.installationDetectedInstall.setOnClickListener {
            dismiss()
            when (app) {
                requireActivity().getString(R.string.vanced) -> startVancedInstall(requireActivity(),
                    getDefaultSharedPreferences(requireActivity()).getString("vanced_variant", "nonroot"))
                requireActivity().getString(R.string.music) -> startMusicInstall(requireActivity())
                requireActivity().getString(R.string.microg) -> startMicrogInstall(requireActivity())
            }
            AppDownloadDialog.newInstance(
                app = app,
                installing = true
            ).show(requireActivity())
        }
    }

}