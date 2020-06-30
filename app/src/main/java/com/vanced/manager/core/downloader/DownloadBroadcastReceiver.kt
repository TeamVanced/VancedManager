package com.vanced.manager.core.downloader

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class DownloadBroadcastReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val type = intent?.getStringExtra("type")
        when (type) {
            "vanced" -> context?.stopService(Intent(context, VancedDownloadService::class.java))
            "microg" -> context?.stopService(Intent(context, MicrogDownloadService::class.java))
        }
        Log.d("VMNotification", "Canceled $type download")
    }
}