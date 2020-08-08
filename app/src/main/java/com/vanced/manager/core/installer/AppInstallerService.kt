package com.vanced.manager.core.installer

import android.app.Service
import android.content.Intent
import android.content.pm.PackageInstaller
import android.os.IBinder
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.vanced.manager.ui.fragments.HomeFragment
import com.vanced.manager.utils.AppUtils.sendFailure

class AppInstallerService: Service() {

    private val localBroadcastManager by lazy { LocalBroadcastManager.getInstance(this) }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        when (intent.getIntExtra(PackageInstaller.EXTRA_STATUS, -999)) {
            PackageInstaller.STATUS_PENDING_USER_ACTION -> {
                Log.d(TAG, "Requesting user confirmation for installation")
                localBroadcastManager.sendBroadcast(Intent(HomeFragment.MICROG_INSTALLING))
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
                with(localBroadcastManager) {
                    sendBroadcast(Intent(HomeFragment.REFRESH_HOME))
                    sendBroadcast(Intent(HomeFragment.MICROG_INSTALLED))
                }
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