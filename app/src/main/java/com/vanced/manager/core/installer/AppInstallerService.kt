package com.vanced.manager.core.installer

import android.app.Service
import android.content.Intent
import android.content.pm.PackageInstaller
import android.os.IBinder
import com.vanced.manager.utils.AppUtils.log
import com.vanced.manager.utils.AppUtils.sendCloseDialog
import com.vanced.manager.utils.AppUtils.sendFailure
import com.vanced.manager.utils.AppUtils.sendRefresh

class AppInstallerService : Service() {

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        when (intent.getIntExtra(PackageInstaller.EXTRA_STATUS, -999)) {
            PackageInstaller.STATUS_PENDING_USER_ACTION -> {
                log(TAG, "Requesting user confirmation for installation")
                val confirmationIntent = intent.getParcelableExtra<Intent>(Intent.EXTRA_INTENT)
                confirmationIntent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                try {
                    startActivity(confirmationIntent)
                } catch (e: Exception) {
                    log("VMInstall", "Unable to start installation")
                }
            }
            PackageInstaller.STATUS_SUCCESS -> {
                log(TAG, "Installation succeed")
                sendCloseDialog(this)
                sendRefresh(this)
            }
            else -> {
                sendCloseDialog(this)
                intent.getStringExtra(PackageInstaller.EXTRA_STATUS_MESSAGE)?.let {
                    sendFailure(it, this)
                }
            }
        }
        stopSelf()
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    companion object {
        const val TAG = "VMInstall"
    }

}
