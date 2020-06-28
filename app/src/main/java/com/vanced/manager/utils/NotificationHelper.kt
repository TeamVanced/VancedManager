package com.vanced.manager.utils

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.vanced.manager.R

object NotificationHelper {

    fun displayDownloadNotif(channel: Int, maxVal: Int = 100, progress:Int = 0, filename: String, context: Context) {
        val notifBuilder =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                Notification.Builder(context, channel.toString()).setChannelId(channel.toString())
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