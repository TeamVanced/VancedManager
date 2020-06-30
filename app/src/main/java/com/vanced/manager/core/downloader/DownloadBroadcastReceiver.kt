package com.vanced.manager.core.downloader

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class DownloadBroadcastReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        when (intent?.getStringExtra("type")) {
            "vanced" -> {
                context.stopService(Intent(context, VancedDownloadService::class.java))
                notificationManager.cancel(69)
                Log.d("VMNotification", "Canceled vanced download")
            }
            "microg" -> {
                notificationManager.cancel(420)
                context.stopService(Intent(context, MicrogDownloadService::class.java))
                Log.d("VMNotification", "Canceled microg download")
            }
        }

    }
}