package com.vanced.manager.ui.dialogs

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.vanced.manager.R
import com.vanced.manager.core.downloader.MicrogDownloader.downloadMicrog
import com.vanced.manager.core.downloader.MusicDownloader.downloadMusic
import com.vanced.manager.core.downloader.VancedDownloader.downloadVanced
import com.vanced.manager.core.ui.base.BindingDialogFragment
import com.vanced.manager.databinding.DialogAppDownloadBinding
import com.vanced.manager.utils.*

class AppDownloadDialog : BindingDialogFragment<DialogAppDownloadBinding>() {

    companion object {

        const val CLOSE_DIALOG = "close_dialog"
        private const val TAG_APP = "TAG_APP"
        private const val TAG_VERSION = "TAG_VERSION"
        private const val TAG_INSTALLING = "TAG_INSTALLING"

        fun newInstance(
            app: String,
            version: String? = null,
            installing: Boolean = false
        ): AppDownloadDialog = AppDownloadDialog().apply {
            arguments = Bundle().apply {
                putString(TAG_APP, app)
                putString(TAG_VERSION, version)
                putBoolean(TAG_INSTALLING, installing)
            }
        }
    }

    private val localBroadcastManager by lazy { LocalBroadcastManager.getInstance(requireActivity()) }

    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == CLOSE_DIALOG) {
                dismiss()
            }
        }
    }

    override fun binding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DialogAppDownloadBinding.inflate(inflater, container, false)

    override fun otherSetups() {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        bindData()
    }

    private fun bindData() {
        with(binding) {
            isCancelable = false
            binding.appDownloadProgressbar.applyAccent()
            binding.appInstallProgressbar.applyAccent()
            bindDownloadProgress()
            val app = arguments?.getString(TAG_APP)
            appDownloadHeader.text = app
            if (arguments?.getBoolean(TAG_INSTALLING) == false) {
                when (app) {
                    getString(R.string.vanced) -> downloadVanced(
                        requireContext(),
                        arguments?.getString(TAG_VERSION)
                    )
                    getString(R.string.music) -> downloadMusic(
                        requireContext(),
                        arguments?.getString(TAG_VERSION)
                    )
                    getString(R.string.microg) -> downloadMicrog(requireContext())
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun DialogAppDownloadBinding.bindDownloadProgress() {
        downloadProgress.observe(viewLifecycleOwner) {
            appDownloadProgressbar.progress = it
            appDownloadProgress.text = "$it%"
        }
        installing.observe(viewLifecycleOwner) { installing ->
            appDownloadProgressbarContainer.isVisible = !installing
            appInstallProgressbar.isVisible = installing
            appDownloadFile.isVisible = !installing
            appDownloadCancel.isEnabled = !installing
            appDownloadCancel.setOnClickListener {
                if (installing) {
                    return@setOnClickListener
                }
                currentDownload?.cancel()
                downloadProgress.value = 0
                dismiss()
            }
        }
        downloadingFile.observe(viewLifecycleOwner) {
            appDownloadFile.text = it
        }
    }

    override fun onResume() {
        super.onResume()
        registerReceiver()
    }

    private fun registerReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(CLOSE_DIALOG)
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter)
    }
}