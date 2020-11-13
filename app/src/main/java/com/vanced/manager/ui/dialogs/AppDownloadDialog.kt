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
import com.vanced.manager.core.downloader.MicrogDownloader.downloadMicrog
import com.vanced.manager.core.downloader.MusicDownloader.downloadMusic
import com.vanced.manager.core.downloader.VancedDownloader.downloadVanced
import com.vanced.manager.databinding.DialogAppDownloadBinding
import com.vanced.manager.utils.DownloadHelper.downloadProgress

class AppDownloadDialog(
    private val app: String,
    private val installing: Boolean = false
) : DialogFragment() {

    private lateinit var binding: DialogAppDownloadBinding

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
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_app_download, container, false)
        binding.progress = downloadProgress.get()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = false
        binding.appDownloadHeader.text = requireActivity().getString(R.string.installing_app, app)
        binding.appDownloadCancel.setOnClickListener {
            if (downloadProgress.get()?.installing?.get() == true)
                return@setOnClickListener

            PRDownloader.cancel(downloadProgress.get()!!.currentDownload)
            dismiss()
        }
        if (!installing) {
            when (app) {
                requireActivity().getString(R.string.vanced) -> downloadVanced(requireActivity())
                requireActivity().getString(R.string.music) -> downloadMusic(requireActivity())
                requireActivity().getString(R.string.microg) -> downloadMicrog(requireActivity())
            }
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

    companion object {
        const val CLOSE_DIALOG = "close_dialog"
    }

}