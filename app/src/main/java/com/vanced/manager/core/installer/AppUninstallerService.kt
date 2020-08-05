package com.vanced.manager.core.installer

import android.app.Service
import android.content.Intent
import android.content.pm.PackageInstaller
import android.os.Handler
import android.os.IBinder
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.vanced.manager.ui.fragments.HomeFragment

class AppUninstallerService: Service() {

    private val localBroadcastManager by lazy { LocalBroadcastManager.getInstance(this) }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val pkgName = intent?.getStringExtra("pkg")
        when (intent?.getIntExtra(PackageInstaller.EXTRA_STATUS, -999)) {
            PackageInstaller.STATUS_PENDING_USER_ACTION -> {
                Log.d(AppInstallerService.TAG, "Requesting user confirmation for uninstallation")
                val confirmationIntent = intent.getParcelableExtra<Intent>(Intent.EXTRA_INTENT)
                confirmationIntent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                try {
                    startActivity(confirmationIntent)
                } catch (e: Exception) {
                }
            }
            PackageInstaller.STATUS_SUCCESS -> {
                Handler().postDelayed({
                    localBroadcastManager.sendBroadcast(Intent(HomeFragment.REFRESH_HOME))
                    Log.d("VMpm", "Successfully uninstalled $pkgName")
                }, 500)
            }
            PackageInstaller.STATUS_FAILURE -> {
                Handler().postDelayed({
                    localBroadcastManager.sendBroadcast(Intent(HomeFragment.REFRESH_HOME))
                    Log.d("VMpm", "Failed to uninstall $pkgName")
                }, 500)
            }
        }
        stopSelf()
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

}