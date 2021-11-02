package com.vanced.manager.core.installer.service

import android.app.Service
import android.content.Intent
import android.content.pm.PackageInstaller
import android.os.IBinder

class AppInstallService : Service() {

    override fun onStartCommand(
        intent: Intent,
        flags: Int,
        startId: Int
    ): Int {
        when (val status = intent.getIntExtra(PackageInstaller.EXTRA_STATUS, -999)) {
            PackageInstaller.STATUS_PENDING_USER_ACTION -> {
                startActivity(
                    intent.getParcelableExtra<Intent>(Intent.EXTRA_INTENT).apply {
                        this?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                )
            }
            else -> {
                sendBroadcast(Intent().apply {
                    action = APP_INSTALL_STATUS
                    putExtra(EXTRA_INSTALL_STATUS, status)
                    putExtra(EXTRA_INSTALL_EXTRA, intent.getStringExtra(PackageInstaller.EXTRA_STATUS_MESSAGE))
                })
            }
        }
        stopSelf()
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        const val APP_INSTALL_STATUS = "APP_INSTALL_STATUS"

        const val EXTRA_INSTALL_STATUS = "EXTRA_INSTALL_STATUS"
        const val EXTRA_INSTALL_EXTRA = "EXTRA_INSTALL_EXTRA"
    }

}