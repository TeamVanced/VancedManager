package com.vanced.manager.core.installer

import android.app.Service
import android.content.Intent
import android.content.pm.PackageInstaller
import android.os.IBinder
import android.util.Log
import com.vanced.manager.ui.viewmodels.HomeViewModel.Companion.microgProgress
import com.vanced.manager.ui.viewmodels.HomeViewModel.Companion.musicProgress
import com.vanced.manager.utils.AppUtils.mutableInstall
import com.vanced.manager.utils.AppUtils.sendFailure
import com.vanced.manager.utils.AppUtils.sendRefresh

class AppInstallerService: Service() {

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val app = intent.getStringExtra("app")
        when (intent.getIntExtra(PackageInstaller.EXTRA_STATUS, -999)) {
            PackageInstaller.STATUS_PENDING_USER_ACTION -> {
                Log.d(TAG, "Requesting user confirmation for installation")
                val confirmationIntent = intent.getParcelableExtra<Intent>(Intent.EXTRA_INTENT)
                confirmationIntent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                try {
                    startActivity(confirmationIntent)
                } catch (e: Exception) {
                    Log.d("VMInstall", "Unable to start installation")
                }
            }
            PackageInstaller.STATUS_SUCCESS -> {
                Log.d(TAG, "Installation succeed")
                if (app == "microg") microgProgress.get()?.showInstallCircle?.set(false) else musicProgress.get()?.showInstallCircle?.set(false)
                mutableInstall.postValue(false)
                sendRefresh(this)
            }
            else -> {
                sendFailure(intent.getIntExtra(PackageInstaller.EXTRA_STATUS, -999), this)
                if (app == "microg") microgProgress.get()?.showInstallCircle?.set(false) else musicProgress.get()?.showInstallCircle?.set(false)
            }
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
