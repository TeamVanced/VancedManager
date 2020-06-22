package com.vanced.manager.core.installer

import android.app.Service
import android.content.Intent
import android.content.pm.PackageInstaller
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.vanced.manager.ui.MainActivity

class AppUninstallerService: Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val pkgName = intent?.getStringExtra("pkg")
        when (intent?.getIntExtra(PackageInstaller.EXTRA_STATUS, -999)) {
            PackageInstaller.STATUS_PENDING_USER_ACTION -> {
                Log.d(AppInstallerService.TAG, "Requesting user confirmation for installation")
                val confirmationIntent = intent.getParcelableExtra<Intent>(Intent.EXTRA_INTENT)
                confirmationIntent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                try {
                    startActivity(confirmationIntent)
                } catch (e: Exception) {
                }
            }
            PackageInstaller.STATUS_SUCCESS -> {
                val mIntent = Intent(MainActivity.APP_UNINSTALLED)
                mIntent.action = MainActivity.APP_UNINSTALLED
                LocalBroadcastManager.getInstance(this).sendBroadcast(mIntent)
                Log.d("VMpm", "Successfully uninstalled $pkgName")
            }
            PackageInstaller.STATUS_FAILURE -> {
                val mIntent = Intent(MainActivity.APP_NOT_UNINSTALLED)
                mIntent.action = MainActivity.APP_NOT_UNINSTALLED
                mIntent.putExtra("pkgName", pkgName)
                LocalBroadcastManager.getInstance(this).sendBroadcast(mIntent)
                Log.d("VMpm", "Failed to uninstall $pkgName")
            }
        }
        stopSelf()
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

}