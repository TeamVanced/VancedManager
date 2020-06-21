package com.vanced.manager.core.installer

import android.app.Service
import android.content.Intent
import android.content.pm.PackageInstaller
import android.os.IBinder
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.vanced.manager.ui.fragments.HomeFragment

class AppUninstallerService: Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.getIntExtra(PackageInstaller.EXTRA_STATUS, -999)) {
            PackageInstaller.STATUS_SUCCESS -> {
                sendBroadCast(HomeFragment.APP_UNINSTALLED)

            }
            PackageInstaller.STATUS_FAILURE -> {
                sendBroadCast(HomeFragment.APP_NOT_UNINSTALLED)
            }
        }
        return START_NOT_STICKY
    }

    private fun sendBroadCast(status: String) {
        val mIntent = Intent(status)
        mIntent.action = status
        mIntent.putExtra("pkgName", PackageInstaller.EXTRA_PACKAGE_NAME)
        LocalBroadcastManager.getInstance(this).sendBroadcast(mIntent)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

}