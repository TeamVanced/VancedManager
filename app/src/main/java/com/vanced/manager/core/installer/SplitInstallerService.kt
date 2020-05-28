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
                mIntent.action = MainActivity.INSTALL_COMPLETED
                LocalBroadcastManager.getInstance(this).sendBroadcast(mIntent)
            }
            PackageInstaller.STATUS_FAILURE_ABORTED -> {
                getSharedPreferences("installPrefs", Context.MODE_PRIVATE).edit().putBoolean("isInstalling", false).apply()
                val mIntent = Intent(MainActivity.INSTALL_ABORTED)
                mIntent.action = MainActivity.INSTALL_ABORTED
                LocalBroadcastManager.getInstance(this).sendBroadcast(mIntent)
            }
            PackageInstaller.STATUS_FAILURE_INVALID -> {
                getSharedPreferences("installPrefs", Context.MODE_PRIVATE).edit().putBoolean("isInstalling", false).apply()
                val mIntent = Intent(MainActivity.INSTALL_INVALID)
                mIntent.action = MainActivity.INSTALL_INVALID
                LocalBroadcastManager.getInstance(this).sendBroadcast(mIntent)
            }
            PackageInstaller.STATUS_FAILURE_CONFLICT -> {
                getSharedPreferences("installPrefs", Context.MODE_PRIVATE).edit().putBoolean("isInstalling", false).apply()
                val mIntent = Intent(MainActivity.INSTALL_CONFLICT)
                mIntent.action = MainActivity.INSTALL_CONFLICT
                LocalBroadcastManager.getInstance(this).sendBroadcast(mIntent)
            }
            PackageInstaller.STATUS_FAILURE_STORAGE -> {
                getSharedPreferences("installPrefs", Context.MODE_PRIVATE).edit().putBoolean("isInstalling", false).apply()
                val mIntent = Intent(MainActivity.INSTALL_STORAGE)
                mIntent.action = MainActivity.INSTALL_STORAGE
                LocalBroadcastManager.getInstance(this).sendBroadcast(mIntent)
            }
            PackageInstaller.STATUS_FAILURE_BLOCKED -> {
                getSharedPreferences("installPrefs", Context.MODE_PRIVATE).edit().putBoolean("isInstalling", false).apply()
                val mIntent = Intent(MainActivity.INSTALL_BLOCKED)
                mIntent.action = MainActivity.INSTALL_BLOCKED
                LocalBroadcastManager.getInstance(this).sendBroadcast(mIntent)
            }
            else -> {
                getSharedPreferences("installPrefs", Context.MODE_PRIVATE).edit().putBoolean("isInstalling", false).apply()
                Log.d(TAG, "Installation failed")
                val mIntent = Intent(MainActivity.INSTALL_FAILED)
                mIntent.action = MainActivity.INSTALL_FAILED
                LocalBroadcastManager.getInstance(this).sendBroadcast(mIntent)
            }
        }
        stopSelf()
        return START_NOT_STICKY
    }

    @Nullable
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    companion object{
        const val TAG = "VMInstall"
    }

}