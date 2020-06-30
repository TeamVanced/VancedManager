package com.vanced.manager.core.downloader

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.downloader.PRDownloader

class DownloadBroadcastReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val tag: Int? = intent?.getIntExtra("tag", 0)
        PRDownloader.cancel(tag)
        Log.d("VMNotification", "Canceled $tag download")
    }
}