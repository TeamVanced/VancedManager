package com.vanced.manager.ui.dialogs

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.downloader.PRDownloader
import com.vanced.manager.R
import com.vanced.manager.core.downloader.MicrogDownloader
import com.vanced.manager.core.downloader.MusicDownloader
import com.vanced.manager.core.downloader.VancedDownloader
import com.vanced.manager.databinding.DialogAppDownloadBinding
import com.vanced.manager.databinding.DialogManagerUpdateBinding
import com.vanced.manager.ui.core.BindingDialogFragment
import com.vanced.manager.utils.DownloadHelper.downloadManager
import com.vanced.manager.utils.DownloadHelper.downloadProgress
import com.vanced.manager.utils.InternetTools.isUpdateAvailable
import kotlinx.coroutines.launch

class ManagerUpdateDialog(
    private val forceUpdate: Boolean
) : BindingDialogFragment<DialogManagerUpdateBinding>()  {

    companion object {

        const val CLOSE_DIALOG = "close_dialog"
        private const val TAG_APP = "TAG_APP"
        private const val TAG_INSTALLING = "TAG_INSTALLING"

        fun newInstance(
            app: String,
            installing: Boolean = false
        ): AppDownloadDialog = AppDownloadDialog().apply {
            arguments = Bundle().apply {
                putString(TAG_APP, app)
                putBoolean(TAG_INSTALLING, installing)
            }
        }
    }

    private val localBroadcastManager by lazy { LocalBroadcastManager.getInstance(requireActivity()) }

    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                CLOSE_DIALOG -> dismiss()
            }
        }
    }

    override fun binding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DialogManagerUpdateBinding.inflate(inflater, container, false)

    override fun otherSetups() {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        bindData()
        lifecycleScope.launch {
            if (forceUpdate) {
                binding.managerUpdatePatient.text = requireActivity().getString(R.string.please_be_patient)
                downloadManager(requireActivity())
            } else {
                checkUpdates()
            }

            binding.managerUpdateCancel.setOnClickListener {
                PRDownloader.cancel(downloadProgress.value?.currentDownload)
                dismiss()
            }
        }
    }

    private fun bindData() {
        with(binding) {
            isCancelable = false
            bindDownloadProgress()
        }
    }

    private fun DialogManagerUpdateBinding.bindDownloadProgress() {
        with(downloadProgress) {
            observe(viewLifecycleOwner) { progressModel ->
                progressModel.downloadProgress.observe(viewLifecycleOwner) {
                    managerUpdateProgressbar.progress = it
                    managerUpdateProgressbar.isVisible = it != 0
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        registerReceiver()
    }

    private suspend fun checkUpdates() {
        if (isUpdateAvailable()) {
            binding.managerUpdatePatient.text = requireActivity().getString(R.string.please_be_patient)
            downloadManager(requireActivity())
        } else {
            binding.managerUpdatePatient.text = requireActivity().getString(R.string.update_not_found)
        }
    }

    private fun registerReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(CLOSE_DIALOG)
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter)
    }
}
