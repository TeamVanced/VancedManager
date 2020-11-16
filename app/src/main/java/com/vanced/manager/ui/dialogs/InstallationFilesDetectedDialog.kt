package com.vanced.manager.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.preference.PreferenceManager.*
import com.vanced.manager.R
import com.vanced.manager.core.downloader.MicrogDownloader.startMicrogInstall
import com.vanced.manager.core.downloader.MusicDownloader.startMusicInstall
import com.vanced.manager.core.downloader.VancedDownloader.startVancedInstall
import com.vanced.manager.databinding.DialogInstallationFilesDetectedBinding
import com.vanced.manager.ui.core.BindingBottomSheetDialogFragment
import com.vanced.manager.utils.Extensions.show

class InstallationFilesDetectedDialog : BindingBottomSheetDialogFragment<DialogInstallationFilesDetectedBinding>() {

    companion object {

        private const val TAG_APP = "TAG_APP"

        fun newInstance(
            app: String
        ): InstallationFilesDetectedDialog = InstallationFilesDetectedDialog().apply {
            arguments = Bundle().apply {
                putString(TAG_APP, app)
            }
        }
    }

    override fun binding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DialogInstallationFilesDetectedBinding.inflate(inflater, container, false)

    override fun otherSetups() {
        bindData()
    }

    private fun bindData() {
        with(binding) {
            val app = arguments?.getString(TAG_APP) ?: throw IllegalArgumentException("app name is null")
            installationDetectedTitle.text = getString(R.string.app_install_files_detected, app)
            installationDetectedSummary.text = getString(R.string.app_install_files_detected_summary, app)
            installationDetectedRedownload.setOnClickListener {
                dismiss()
                if (app == getString(R.string.vanced))
                    VancedPreferencesDialog().show(requireActivity())
                else {
                    AppDownloadDialog.newInstance(app).show(requireActivity())
                }
            }
            installationDetectedInstall.setOnClickListener {
                dismiss()
                when (app) {
                    getString(R.string.vanced) -> startVancedInstall(requireContext(),
                        getDefaultSharedPreferences(requireContext()).getString("vanced_variant", "nonroot"))
                    getString(R.string.music) -> startMusicInstall(requireContext())
                    getString(R.string.microg) -> startMicrogInstall(requireContext())
                }
                AppDownloadDialog.newInstance(
                    app = app,
                    installing = true
                ).show(requireActivity())
            }
        }
    }
}