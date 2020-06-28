package com.vanced.manager.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.vanced.manager.R

object NotificationHelper {

    fun createNotifChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notifChannel = NotificationChannel(
                "69420",
                context.getString(R.string.notif_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notifChannel)
        }
    }

    fun displayDownloadNotif(channel: Int, maxVal: Int = 100, progress:Int = 0, filename: String, context: Context) {
        val notifBuilder =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                Notification.Builder(context, channel.toString()).setChannelId("69420")
            else
                Notification.Builder(context).setPriority(Notification.PRIORITY_DEFAULT)

        notifBuilder.apply {
            setContentTitle(context.getString(R.string.app_name))
            setContentText(context.getString(R.string.downloading_file, filename))
            setSmallIcon(R.drawable.ic_stat_name)
        }
        val notif = notifBuilder.build()
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.apply {
            notifBuilder.setProgress(maxVal, progress, false)
            notify(channel, notif)
        }


    }

}