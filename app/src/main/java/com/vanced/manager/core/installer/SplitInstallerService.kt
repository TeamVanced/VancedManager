package com.vanced.manager.core.installer

import android.app.Service
import android.content.Intent
import android.content.pm.PackageInstaller
import android.os.IBinder
import android.util.Log
import com.vanced.manager.ui.viewmodels.HomeViewModel.Companion.vancedProgress
import com.vanced.manager.utils.AppUtils.sendFailure
import com.vanced.manager.utils.AppUtils.sendRefresh

class SplitInstallerService: Service() {

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        when (intent.getIntExtra(PackageInstaller.EXTRA_STATUS, -999)) {
            PackageInstaller.STATUS_PENDING_USER_ACTION -> {
                Log.d(TAG, "Requesting user confirmation for installation")
                val confirmationIntent = intent.getParcelableExtra<Intent>(Intent.EXTRA_INTENT)
                confirmationIntent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                try {
                    startActivity(confirmationIntent)
                } catch (e: Exception) {
                }
            }
            PackageInstaller.STATUS_SUCCESS -> {
                Log.d(TAG, "Installation succeed")
                vancedProgress.get()?.showInstallCircle = false
                sendRefresh(this)
            }
            else -> sendFailure(intent.getIntExtra(PackageInstaller.EXTRA_STATUS, -999), this)

        }
        stopSelf()
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    companion object{
        const val TAG = "VMInstall"
    }

}
