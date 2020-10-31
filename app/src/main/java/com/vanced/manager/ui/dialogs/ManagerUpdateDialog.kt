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
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.downloader.PRDownloader
import com.vanced.manager.R
import com.vanced.manager.databinding.DialogManagerUpdateBinding
import com.vanced.manager.utils.DownloadHelper.downloadManager
import com.vanced.manager.utils.DownloadHelper.downloadProgress
import com.vanced.manager.utils.InternetTools.isUpdateAvailable

class ManagerUpdateDialog(
        private val forceUpdate: Boolean
) : DialogFragment() {

    private lateinit var binding: DialogManagerUpdateBinding

    private val localBroadcastManager by lazy { LocalBroadcastManager.getInstance(requireActivity()) }

    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                CLOSE_DIALOG -> dismiss()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (dialog != null && dialog?.window != null) {
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        isCancelable = false
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_manager_update, container, false)
        binding.progress = downloadProgress.get()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (forceUpdate) {
            binding.managerUpdatePatient.text = requireActivity().getString(R.string.please_be_patient)
            downloadManager(requireActivity())
        } else
            checkUpdates()

        binding.managerUpdateCancel.setOnClickListener {
            PRDownloader.cancel(downloadProgress.get()!!.currentDownload)
            dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        registerReceiver()
    }

    private fun checkUpdates() {
        if (isUpdateAvailable(requireActivity())) {
            binding.managerUpdatePatient.text = requireActivity().getString(R.string.please_be_patient)
            downloadManager(requireActivity())
        } else
            binding.managerUpdatePatient.text = requireActivity().getString(R.string.update_not_found)
    }

    private fun registerReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(CLOSE_DIALOG)
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter)
    }

    companion object {
        const val CLOSE_DIALOG = "close_dialog"
    }

}
