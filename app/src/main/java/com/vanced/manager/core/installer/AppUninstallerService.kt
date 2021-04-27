package com.vanced.manager.core.installer

import android.app.Service
import android.content.Intent
import android.content.pm.PackageInstaller
import android.os.IBinder
import com.vanced.manager.utils.AppUtils.log
import com.vanced.manager.utils.AppUtils.sendRefresh

class AppUninstallerService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val pkgName = intent?.getStringExtra("pkg")
        when (intent?.getIntExtra(PackageInstaller.EXTRA_STATUS, -999)) {
            PackageInstaller.STATUS_PENDING_USER_ACTION -> {
                log(AppInstallerService.TAG, "Requesting user confirmation for uninstallation")
                val confirmationIntent = intent.getParcelableExtra<Intent>(Intent.EXTRA_INTENT)
                confirmationIntent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                try {
                    startActivity(confirmationIntent)
                } catch (e: Exception) {
                }
            }
            //Delay broadcast until activity (and fragment) show up on the screen
            PackageInstaller.STATUS_SUCCESS -> {
                sendRefresh(this)
                log("VMpm", "Successfully uninstalled $pkgName")
            }
            PackageInstaller.STATUS_FAILURE -> {
                sendRefresh(this)
                log("VMpm", "Failed to uninstall $pkgName")
            }
        }
        stopSelf()
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

}
