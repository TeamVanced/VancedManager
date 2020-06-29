package com.vanced.manager.core.downloader

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.downloader.PRDownloader

class DownloadBroadcastReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            "cancel" -> PRDownloader.cancel(intent.getStringExtra("dwnldId"))
        }

    }
}