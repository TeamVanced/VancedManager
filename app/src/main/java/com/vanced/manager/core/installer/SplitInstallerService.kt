package com.vanced.manager.core.installer

import android.app.Service
import android.content.Intent
import android.content.pm.PackageInstaller
import android.os.IBinder
import android.util.Log

class SplitInstallerService: Service() {

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        when (
            if (intent.hasExtra(PackageInstaller.EXTRA_STATUS))
            null
        else
            intent.getIntExtra(PackageInstaller.EXTRA_STATUS, 0)) {
            PackageInstaller.STATUS_PENDING_USER_ACTION -> {
                val confirmationIntent = intent.getParcelableExtra<Intent>(Intent.EXTRA_INTENT)
                confirmationIntent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                try {
                    startActivity(confirmationIntent)
                } catch (e: Exception) {
                }
            }
            PackageInstaller.STATUS_SUCCESS -> Log.d("AppLog", "Installation succeed")
            else -> Log.d("VMInstall", "Installation failed")
        }
        stopSelf()
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

}