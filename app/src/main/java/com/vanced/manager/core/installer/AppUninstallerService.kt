package com.vanced.manager.core.installer

import android.app.Service
import android.content.Intent
import android.content.pm.PackageInstaller
import android.os.IBinder
import android.util.Log
import com.vanced.manager.ui.viewmodels.HomeViewModel.Companion.fetchData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AppUninstallerService: Service() {

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
            //Delay broadcast until activity (and fragment) show up on the screen
            PackageInstaller.STATUS_SUCCESS -> {
                CoroutineScope(Dispatchers.IO).launch {
                    delay(500)
                    fetchData()
                    Log.d("VMpm", "Successfully uninstalled $pkgName")
                }
            }
            PackageInstaller.STATUS_FAILURE -> {
                CoroutineScope(Dispatchers.IO).launch {
                    delay(500)
                    fetchData()
                    Log.d("VMpm", "Failed to uninstall $pkgName")
                }
            }
        }
        stopSelf()
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

}
