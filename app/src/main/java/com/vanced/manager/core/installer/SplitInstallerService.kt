package com.vanced.manager.core.installer

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInstaller
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.vanced.manager.ui.MainActivity

class SplitInstallerService: Service() {
    private val TAG = "VMInstall"

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        when (intent.getIntExtra(PackageInstaller.EXTRA_STATUS, -999)) {
            PackageInstaller.STATUS_PENDING_USER_ACTION -> {
                Toast.makeText(this, "Installing...", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "Requesting user confirmation for installation")
                val confirmationIntent =
                    intent.getParcelableExtra<Intent>(Intent.EXTRA_INTENT)
                confirmationIntent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                try {
                    startActivity(confirmationIntent)
                } catch (e: Exception) {
                }
            }
            PackageInstaller.STATUS_SUCCESS -> {
                Log.d(TAG, "Installation succeed")
                getSharedPreferences("installPrefs", Context.MODE_PRIVATE).edit().putBoolean("isInstalling", false).apply()
                val mIntent = Intent(MainActivity.INSTALL_COMPLETED)
                sendBroadcast(mIntent)
            }
            PackageInstaller.STATUS_FAILURE_ABORTED -> {
                val mIntent = Intent(MainActivity.INSTALL_ABORTED)
                sendBroadcast(mIntent)
            }
            PackageInstaller.STATUS_FAILURE_INVALID -> {
                val mIntent = Intent(MainActivity.INSTALL_INVALID)
                sendBroadcast(mIntent)
            }
            PackageInstaller.STATUS_FAILURE_CONFLICT -> {
                val mIntent = Intent(MainActivity.INSTALL_CONFLICT)
                sendBroadcast(mIntent)
            }
            PackageInstaller.STATUS_FAILURE_STORAGE -> {
                val mIntent = Intent(MainActivity.INSTALL_STORAGE)
                sendBroadcast(mIntent)
            }
            PackageInstaller.STATUS_FAILURE_BLOCKED -> {
                val mIntent = Intent(MainActivity.INSTALL_BLOCKED)
                sendBroadcast(mIntent)
            }
            else -> {
                Log.d(TAG, "Installation failed")
                val mIntent = Intent(MainActivity.INSTALL_FAILED)
                sendBroadcast(mIntent)
            }
        }
        stopSelf()
        return START_NOT_STICKY
    }

    @Nullable
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

}