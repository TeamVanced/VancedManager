package com.vanced.manager.core.downloader

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.downloader.PRDownloader

class DownloadBroadcastReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        PRDownloader.cancel(intent?.getStringExtra("dwnldId"))
        Log.d("VMNotification", "Canceled ${intent?.getStringExtra("dwnldId")} download")

    }
}